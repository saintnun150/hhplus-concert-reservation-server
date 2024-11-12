package org.lowell.concert.application.payment.integration;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lowell.concert.application.payment.PaymentFacade;
import org.lowell.concert.application.support.DatabaseCleanUp;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.concert.exception.ConcertReservationError;
import org.lowell.concert.domain.concert.model.ConcertReservation;
import org.lowell.concert.domain.concert.model.ConcertSeat;
import org.lowell.concert.domain.concert.model.ReservationStatus;
import org.lowell.concert.domain.concert.model.SeatStatus;
import org.lowell.concert.domain.user.model.User;
import org.lowell.concert.domain.user.model.UserAccount;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueCommand;
import org.lowell.concert.domain.waitingqueue.model.WaitingQueueTokenInfo;
import org.lowell.concert.domain.waitingqueue.repository.WaitingQueueRepository;
import org.lowell.concert.infra.db.concert.repository.ConcertReservationJpaRepository;
import org.lowell.concert.infra.db.concert.repository.ConcertSeatJpaRepository;
import org.lowell.concert.infra.db.user.repository.UserAccountJpaRepository;
import org.lowell.concert.infra.db.user.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class PaymentConcurrencyTest {

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
    private WaitingQueueRepository waitingQueueRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.execute();
    }


    @DisplayName("같은 예약 건에 대해 결제 요청이 동시에 들어오면 하나만 성공한다.")
    @Test
    void payConcertReservationConcurrently() throws InterruptedException {
        // given
        Long concertScheduleId = 1L;
        int price = 10000;
        long balance = 20000;

        User user = userJpaRepository.save(User.builder()
                                               .username("name")
                                               .build());
        UserAccount account = userAccountJpaRepository.save(UserAccount.builder()
                                                                       .userId(user.getUserId())
                                                                       .balance(balance)
                                                                       .build());

        WaitingQueueTokenInfo token = waitingQueueRepository.createQueueToken(new WaitingQueueCommand.Create("token"));


        ConcertSeat seat = concertSeatJpaRepository.save(ConcertSeat.builder()
                                                                    .seatNo(1)
                                                                    .concertScheduleId(concertScheduleId)
                                                                    .status(SeatStatus.OCCUPIED)
                                                                    .price(price)
                                                                    .tempReservedAt(LocalDateTime.now().minusMinutes(3))
                                                                    .build());

        ConcertReservation reservation = concertReservationJpaRepository.save(ConcertReservation.builder()
                                                                                                .userId(user.getUserId())
                                                                                                .seatId(seat.getSeatId())
                                                                                                .status(ReservationStatus.PENDING)
                                                                                                .build());
        // when
        int paymentCnt = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(paymentCnt);
        CountDownLatch latch = new CountDownLatch(paymentCnt);

        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger failed = new AtomicInteger(0);

        for (int i = 0; i < paymentCnt; i++) {
            executorService.submit(() -> {
                try {
                    paymentFacade.payment(reservation.getReservationId(), token.getToken());
                    success.incrementAndGet();
                } catch (Exception e) {
                    log.error("## errorMsg:[{}]", e.getClass().getSimpleName());
                    if (e instanceof DomainException && ((DomainException) e).getDomainError() == ConcertReservationError.STATE_COMPLETE) {
                        failed.incrementAndGet();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        // then
        assertThat(success.get()).isEqualTo(1);
        assertThat(failed.get()).isEqualTo(9);

    }
}
