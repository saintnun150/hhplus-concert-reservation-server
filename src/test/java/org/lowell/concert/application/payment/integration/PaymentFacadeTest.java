package org.lowell.concert.application.payment.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lowell.concert.application.payment.PaymentFacade;
import org.lowell.concert.application.support.DatabaseCleanUp;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.concert.exception.ConcertReservationError;
import org.lowell.concert.domain.concert.exception.ConcertSeatError;
import org.lowell.concert.domain.concert.model.ConcertReservation;
import org.lowell.concert.domain.concert.model.ConcertSeat;
import org.lowell.concert.domain.concert.model.ReservationStatus;
import org.lowell.concert.domain.concert.model.SeatStatus;
import org.lowell.concert.domain.user.exception.UserAccountError;
import org.lowell.concert.domain.user.model.User;
import org.lowell.concert.domain.user.model.UserAccount;
import org.lowell.concert.domain.waitingqueue.exception.WaitingQueueError;
import org.lowell.concert.domain.waitingqueue.model.TokenStatus;
import org.lowell.concert.domain.waitingqueue.model.WaitingQueue;
import org.lowell.concert.infra.db.concert.repository.ConcertReservationJpaRepository;
import org.lowell.concert.infra.db.concert.repository.ConcertSeatJpaRepository;
import org.lowell.concert.infra.db.payment.repository.PaymentJpaRepository;
import org.lowell.concert.infra.db.user.repository.UserAccountJpaRepository;
import org.lowell.concert.infra.db.user.repository.UserJpaRepository;
import org.lowell.concert.infra.db.waitingqueue.WaitingQueueJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class PaymentFacadeTest {
    private final Logger log = Logger.getLogger(this.getClass().getName());
    @Autowired
    private PaymentFacade paymentFacade;
    @Autowired
    private ConcertSeatJpaRepository concertSeatJpaRepository;
    @Autowired
    private ConcertReservationJpaRepository concertReservationJpaRepository;
    @Autowired
    private WaitingQueueJpaRepository waitingQueueJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private UserAccountJpaRepository userAccountJpaRepository;
    @Autowired
    private PaymentJpaRepository paymentJpaRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.execute();
    }

    @DisplayName("예약된 정보가 없으면 결제에 실패한다.")
    @Test
    void throwException_WhenReservationNotFound() {
        // given
        Long reservationId = 1L;
        String token = "token";

        assertThatThrownBy(() -> paymentFacade.payment(reservationId, token))
                .isInstanceOfSatisfying(DomainException.class, ex -> {
                    assertThat(ex.getDomainError()).isEqualTo(ConcertReservationError.NOT_FOUND_RESERVATION);
                });
    }

    @DisplayName("예약된 좌석 정보가 잘못되었으면 결제에 실패한다.")
    @Test
    void throwException_WhenConcertSeatIsInvalid() {
        // given
        ConcertReservation saved = concertReservationJpaRepository.save(ConcertReservation.builder()
                                                                                          .userId(1L)
                                                                                          .seatId(1L)
                                                                                          .status(ReservationStatus.PENDING)
                                                                                          .build());

        assertThatThrownBy(() -> paymentFacade.payment(saved.getReservationId(), "token"))
                .isInstanceOfSatisfying(DomainException.class, ex -> {
                    assertThat(ex.getDomainError()).isEqualTo(ConcertSeatError.NOT_FOUND_SEAT);
                });
    }

    @DisplayName("예약한 좌석이 만료되면 결제에 실패한다.")
    @Test
    void throwException_WhenConcertSeatIsExpired() {
        // given
        ConcertSeat seat = concertSeatJpaRepository.save(ConcertSeat.builder()
                                                                    .seatNo(1)
                                                                    .concertScheduleId(1L)
                                                                    .status(SeatStatus.OCCUPIED)
                                                                    .price(10000)
                                                                    .tempReservedAt(LocalDateTime.now().minusMinutes(15))
                                                                    .build());
        ConcertReservation saved = concertReservationJpaRepository.save(ConcertReservation.builder()
                                                                                         .userId(1L)
                                                                                         .seatId(seat.getSeatId())
                                                                                         .status(ReservationStatus.PENDING)
                                                                                         .build());

        assertThatThrownBy(() -> paymentFacade.payment(saved.getReservationId(), "token"))
                .isInstanceOfSatisfying(DomainException.class, ex -> {
                    assertThat(ex.getDomainError()).isEqualTo(ConcertSeatError.RESERVED_EXPIRED);
                });
    }

    @DisplayName("예약한 좌석이 이미 결제되었으면 결제에 실패한다.")
    @Test
    void throwException_WhenConcertSeatIsAlreadyPaid() {
        // given
        Long userId = 1L;
        Long concertScheduleId = 1L;
        int price = 10000;
        Long reservationId = 1L;
        ConcertSeat seat = concertSeatJpaRepository.save(ConcertSeat.builder()
                                                                    .seatNo(1)
                                                                    .concertScheduleId(concertScheduleId)
                                                                    .status(SeatStatus.OCCUPIED)
                                                                    .price(price)
                                                                    .tempReservedAt(LocalDateTime.now().minusMinutes(3))
                                                                    .reservedAt(LocalDateTime.now())
                                                                    .build());

        ConcertReservation saved = concertReservationJpaRepository.save(ConcertReservation.builder()
                                                                                          .reservationId(reservationId)
                                                                                          .userId(userId)
                                                                                          .seatId(seat.getSeatId())
                                                                                          .status(ReservationStatus.PENDING)
                                                                                          .build());

        assertThatThrownBy(() -> paymentFacade.payment(saved.getReservationId(), "token"))
                .isInstanceOfSatisfying(DomainException.class, ex -> {
                    assertThat(ex.getDomainError()).isEqualTo(ConcertSeatError.RESERVED_COMPLETE);
                });
    }

    @DisplayName("계좌에 잔액이 부족하면 결제에 실패한다.")
    @Test
    void throwException_WhenUserAccountIsNotEnough() {
        // given
        Long concertScheduleId = 1L;
        int price = 10000;
        long balance = 5000;
        User user = userJpaRepository.save(User.builder()
                                               .username("name")
                                               .build());
        UserAccount account = userAccountJpaRepository.save(UserAccount.builder()
                                                                       .userId(user.getUserId())
                                                                       .balance(balance)
                                                                       .build());
        ConcertSeat seat = concertSeatJpaRepository.save(ConcertSeat.builder()
                                                                    .seatNo(1)
                                                                    .concertScheduleId(concertScheduleId)
                                                                    .status(SeatStatus.OCCUPIED)
                                                                    .price(price)
                                                                    .tempReservedAt(LocalDateTime.now().minusMinutes(3))
                                                                    .build());
        ConcertReservation saved = concertReservationJpaRepository.save(ConcertReservation.builder()
                                                                                          .userId(user.getUserId())
                                                                                          .seatId(seat.getSeatId())
                                                                                          .status(ReservationStatus.PENDING)
                                                                                          .build());


        assertThatThrownBy(() -> paymentFacade.payment(saved.getReservationId(), "token"))
                .isInstanceOfSatisfying(DomainException.class, ex -> {
                    assertThat(ex.getDomainError()).isEqualTo(UserAccountError.EXCEED_BALANCE);
                });
    }

    @DisplayName("예약 상태가 완료됐지만 해당 토큰이 만료된 상태라면 결제에 실패한다.")
    @Test
    void throwException_WhenTokenIsExpired() {
        // given
        Long concertScheduleId = 1L;
        int price = 10000;
        long balance = 20000;
        User user = userJpaRepository.save(User.builder()
                                               .username("name")
                                               .build());
        userAccountJpaRepository.save(UserAccount.builder()
                                                 .userId(user.getUserId())
                                                 .balance(balance)
                                                 .build());
        ConcertSeat seat = concertSeatJpaRepository.save(ConcertSeat.builder()
                                                                    .seatNo(1)
                                                                    .concertScheduleId(concertScheduleId)
                                                                    .status(SeatStatus.OCCUPIED)
                                                                    .price(price)
                                                                    .tempReservedAt(LocalDateTime.now().minusMinutes(3))
                                                                    .build());
        ConcertReservation saved = concertReservationJpaRepository.save(ConcertReservation.builder()
                                                                                          .userId(user.getUserId())
                                                                                          .seatId(seat.getSeatId())
                                                                                          .status(ReservationStatus.PENDING)
                                                                                          .build());

        waitingQueueJpaRepository.save(WaitingQueue.builder()
                                                   .tokenId(1L)
                                                   .token("token")
                                                   .tokenStatus(TokenStatus.ACTIVATE)
                                                   .createdAt(LocalDateTime.now().minusMinutes(5))
                                                   .expiresAt(LocalDateTime.now().minusMinutes(20))
                                                   .build());

        assertThatThrownBy(() -> paymentFacade.payment(saved.getReservationId(), "token"))
                .isInstanceOfSatisfying(DomainException.class, ex -> {
                    assertThat(ex.getDomainError()).isEqualTo(WaitingQueueError.TOKEN_EXPIRED);
                });
    }





}
