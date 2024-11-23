package org.lowell.apps.payment.application.integration;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lowell.apps.common.util.UUIDGenerator;
import org.lowell.apps.concert.domain.model.ConcertReservation;
import org.lowell.apps.concert.domain.model.ConcertSeat;
import org.lowell.apps.concert.domain.model.ReservationStatus;
import org.lowell.apps.concert.domain.model.SeatStatus;
import org.lowell.apps.concert.infra.repository.ConcertReservationJpaRepository;
import org.lowell.apps.concert.infra.repository.ConcertSeatJpaRepository;
import org.lowell.apps.payment.application.PaymentFacade;
import org.lowell.apps.payment.domain.model.EventStatus;
import org.lowell.apps.payment.domain.model.EventType;
import org.lowell.apps.payment.infra.repository.PaymentOutBoxJpaRepository;
import org.lowell.apps.support.DatabaseCleanUp;
import org.lowell.apps.user.domain.model.User;
import org.lowell.apps.user.domain.model.UserAccount;
import org.lowell.apps.user.infra.repository.UserAccountJpaRepository;
import org.lowell.apps.user.infra.repository.UserJpaRepository;
import org.lowell.apps.waitingqueue.domain.dto.WaitingQueueCommand;
import org.lowell.apps.waitingqueue.domain.dto.WaitingQueueQuery;
import org.lowell.apps.waitingqueue.domain.model.WaitingQueueTokenInfo;
import org.lowell.apps.waitingqueue.domain.repository.WaitingQueueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.await;

@Slf4j
@SpringBootTest
public class PaymentFacadeKafkaTest {
    @Autowired
    private PaymentFacade paymentFacade;
    @Autowired
    private ConcertSeatJpaRepository concertSeatJpaRepository;
    @Autowired
    private ConcertReservationJpaRepository concertReservationJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private UserAccountJpaRepository userAccountJpaRepository;
    @Autowired
    private PaymentOutBoxJpaRepository paymentOutBoxJpaRepository;
    @Autowired
    private WaitingQueueRepository waitingQueueRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.execute();
    }

    @DisplayName("결제 완료 후 완료 이벤트에 대해 카프카로 발행되며 컨슈머에서 정상 처리될 경우 해당 토큰이 만료된다")
    @Test
    void shouldExpireTokenWhenPaymentCompletionEventIsProcessed() {

        int price = 10000;
        long balance = 100000;
        long concertScheduleId = 1L;
        long seatId = 1L;
        String token = UUIDGenerator.generateTimestampUUID();
        waitingQueueRepository.createQueueToken(new WaitingQueueCommand.Create(token));

        waitingQueueRepository.findWaitingQueueToken(new WaitingQueueQuery.GetToken(token));
        waitingQueueRepository.activateWaitingToken(new WaitingQueueQuery.GetQueues(10, LocalDateTime.now().plusMinutes(10)));


        User user = userJpaRepository.save(User.builder()
                                               .username("name")
                                               .build());
        userAccountJpaRepository.save(UserAccount.builder()
                                                 .userId(user.getUserId())
                                                 .balance(balance)
                                                 .build());

        concertSeatJpaRepository.save(ConcertSeat.builder()
                                                 .seatNo(1)
                                                 .concertScheduleId(concertScheduleId)
                                                 .status(SeatStatus.OCCUPIED)
                                                 .price(price)
                                                 .tempReservedAt(LocalDateTime.now().minusMinutes(3))
                                                 .build());

        ConcertReservation saved = concertReservationJpaRepository.save(ConcertReservation.builder()
                                                                                          .userId(user.getUserId())
                                                                                          .seatId(seatId)
                                                                                          .status(ReservationStatus.PENDING)
                                                                                          .build());

        paymentFacade.payment(saved.getReservationId(), token);

        await().pollDelay(7, SECONDS)
               .atMost(10, SECONDS)
               .untilAsserted(() -> {
                   Optional<WaitingQueueTokenInfo> getToken = waitingQueueRepository.findWaitingQueueToken(new WaitingQueueQuery.GetToken(token));
                   assertThat(getToken).isEmpty();
               });

    }


    @DisplayName("결제 완료 후 완료 이벤트에 대해 카프카로 발행되며 컨슈머에서 정상 처리될 경우 이벤트 상태가 완료로 변경된다.")
    @Test
    void createPaymentEvent() {
        int price = 10000;
        long balance = 100000;
        long concertScheduleId = 1L;
        long seatId = 1L;
        User user = userJpaRepository.save(User.builder()
                                               .username("name")
                                               .build());
        userAccountJpaRepository.save(UserAccount.builder()
                                                 .userId(user.getUserId())
                                                 .balance(balance)
                                                 .build());

        concertSeatJpaRepository.save(ConcertSeat.builder()
                                                 .seatNo(1)
                                                 .concertScheduleId(concertScheduleId)
                                                 .status(SeatStatus.OCCUPIED)
                                                 .price(price)
                                                 .tempReservedAt(LocalDateTime.now().minusMinutes(3))
                                                 .build());

        ConcertReservation saved = concertReservationJpaRepository.save(ConcertReservation.builder()
                                                                                          .userId(user.getUserId())
                                                                                          .seatId(seatId)
                                                                                          .status(ReservationStatus.PENDING)
                                                                                          .build());

        paymentFacade.payment(saved.getReservationId(), "testToken1");

        await().pollDelay(7, SECONDS)
               .atMost(10, SECONDS)
               .untilAsserted(() -> {
                   assertThat(paymentOutBoxJpaRepository.findAll()).isNotEmpty();
                   assertThat(paymentOutBoxJpaRepository.findAll().get(0).getEventType()).isEqualTo(EventType.PAYMENT_COMPLETED);
                   assertThat(paymentOutBoxJpaRepository.findAll().get(0).getEventStatus()).isEqualTo(EventStatus.SUCCESS);
               });
    }


}
