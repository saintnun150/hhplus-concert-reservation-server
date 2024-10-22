package org.lowell.concert.application.payment;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.concert.dto.ConcertReservationCommand;
import org.lowell.concert.domain.concert.exception.ConcertReservationErrorCode;
import org.lowell.concert.domain.concert.exception.ConcertSeatErrorCode;
import org.lowell.concert.domain.concert.model.ConcertReservation;
import org.lowell.concert.domain.concert.model.ConcertSeat;
import org.lowell.concert.domain.concert.model.SeatStatus;
import org.lowell.concert.domain.concert.service.ConcertReservationService;
import org.lowell.concert.domain.concert.service.ConcertSeatService;
import org.lowell.concert.domain.payment.service.PaymentService;
import org.lowell.concert.domain.user.service.UserAccountService;
import org.lowell.concert.domain.user.service.UserService;
import org.lowell.concert.domain.waitingqueue.service.WaitingQueueService;
import org.lowell.concert.infra.db.concert.repository.ConcertSeatJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class PaymentFacadeTest {

    @Autowired
    private PaymentFacade paymentFacade;

    @Autowired
    public PaymentService paymentService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private ConcertSeatService concertSeatService;

    @Autowired
    private ConcertSeatJpaRepository concertSeatJpaRepository;

    @Autowired
    private ConcertReservationService concertReservationService;

    @Autowired
    private WaitingQueueService waitingQueueService;

    @AfterEach
    public void tearDown() {
        paymentService.deleteAll();
        userService.deleteAllUser();
        userAccountService.deleteAll();
        concertSeatService.deleteAll();
        concertReservationService.deleteAll();
        waitingQueueService.deleteAll();
    }

    @DisplayName("예약된 정보가 없으면 결제에 실패한다.")
    @Test
    void throwException_WhenReservationNotFound() {
        // given
        Long reservationId = 1L;
        String token = "token";

        assertThatThrownBy(() -> paymentFacade.payment(reservationId, token))
                .isInstanceOfSatisfying(DomainException.class, ex -> {
                    assertThat(ex.getErrorCode()).isEqualTo(ConcertReservationErrorCode.NOT_FOUND_RESERVATION);
                });
    }

    @DisplayName("예약된 좌석 정보가 잘못되었으면 결제에 실패한다.")
    @Test
    void throwException_WhenConcertSeatIsInvalid() {
        // given
        Long reservationId = 1L;
        ConcertReservation reservation = concertReservationService.createConcertReservation(new ConcertReservationCommand.Create(1L, 1L));

        assertThatThrownBy(() -> paymentFacade.payment(reservation.getReservationId(), "token"))
                .isInstanceOfSatisfying(DomainException.class, ex -> {
                    assertThat(ex.getErrorCode()).isEqualTo(ConcertSeatErrorCode.NOT_FOUND_SEAT);
                });
    }

    @DisplayName("예약한 좌석이 만료되면 결제에 실패한다.")
    @Test
    void throwException_WhenConcertSeatIsExpired() {
        // given
        concertSeatJpaRepository.save(ConcertSeat.builder()
                                                 .seatId(1L)
                                                 .seatNo(1)
                                                 .concertScheduleId(1L)
                                                 .status(SeatStatus.OCCUPIED)
                                                 .price(10000)
                                                 .tempReservedAt(LocalDateTime.now().minusMinutes(15))
                                                 .build());
        ConcertReservation reservation = concertReservationService.createConcertReservation(new ConcertReservationCommand.Create(1L, 1L));

        assertThatThrownBy(() -> paymentFacade.payment(reservation.getReservationId(), "token"))
                .isInstanceOfSatisfying(DomainException.class, ex -> {
                    assertThat(ex.getErrorCode()).isEqualTo(ConcertSeatErrorCode.RESERVED_EXPIRED);
                });
    }
}
