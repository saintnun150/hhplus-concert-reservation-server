package org.lowell.apps.payment.application.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lowell.apps.common.exception.DomainException;
import org.lowell.apps.concert.domain.exception.ConcertReservationError;
import org.lowell.apps.concert.domain.exception.ConcertSeatError;
import org.lowell.apps.concert.domain.model.ConcertReservation;
import org.lowell.apps.concert.domain.model.ConcertSeat;
import org.lowell.apps.concert.domain.model.ReservationStatus;
import org.lowell.apps.concert.domain.model.SeatStatus;
import org.lowell.apps.concert.infra.repository.ConcertReservationJpaRepository;
import org.lowell.apps.concert.infra.repository.ConcertSeatJpaRepository;
import org.lowell.apps.payment.application.PaymentFacade;
import org.lowell.apps.support.DatabaseCleanUp;
import org.lowell.apps.user.domain.exception.UserAccountError;
import org.lowell.apps.user.domain.model.User;
import org.lowell.apps.user.domain.model.UserAccount;
import org.lowell.apps.user.infra.repository.UserAccountJpaRepository;
import org.lowell.apps.user.infra.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class PaymentFacadeTest {
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


        assertThatThrownBy(() -> paymentFacade.payment(saved.getReservationId(), "token"))
                .isInstanceOfSatisfying(DomainException.class, ex -> {
                    assertThat(ex.getDomainError()).isEqualTo(UserAccountError.EXCEED_BALANCE);
                });
    }

    @DisplayName("결제 생성 확인")
    @Test
    void createPayment() {
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

        ConcertSeat seat = concertSeatJpaRepository.save(ConcertSeat.builder()
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

        paymentFacade.payment(saved.getReservationId(), "lowellToken");

    }
}
