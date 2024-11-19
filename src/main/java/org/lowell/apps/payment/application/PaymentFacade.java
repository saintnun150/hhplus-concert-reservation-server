package org.lowell.apps.payment.application;

import lombok.RequiredArgsConstructor;
import org.lowell.apps.concert.domain.ConcertPolicy;
import org.lowell.apps.concert.domain.dto.ConcertReservationQuery;
import org.lowell.apps.concert.domain.dto.ConcertSeatQuery;
import org.lowell.apps.concert.domain.model.ConcertReservation;
import org.lowell.apps.concert.domain.model.ConcertSeat;
import org.lowell.apps.concert.domain.service.ConcertReservationService;
import org.lowell.apps.concert.domain.service.ConcertSeatService;
import org.lowell.apps.payment.domain.dto.PaymentCommand;
import org.lowell.apps.payment.domain.event.PaymentEventPublisher;
import org.lowell.apps.payment.domain.model.Payment;
import org.lowell.apps.payment.domain.model.PaymentStatus;
import org.lowell.apps.payment.domain.service.PaymentService;
import org.lowell.apps.common.lock.DistributedLock;
import org.lowell.apps.user.domain.model.User;
import org.lowell.apps.user.domain.model.UserAccount;
import org.lowell.apps.user.domain.service.UserAccountService;
import org.lowell.apps.user.domain.service.UserService;
import org.lowell.apps.waitingqueue.domain.event.WaitingQueueEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PaymentFacade {
    private final PaymentService paymentService;
    private final UserService userService;
    private final UserAccountService userAccountService;
    private final ConcertSeatService concertSeatService;
    private final ConcertReservationService concertReservationService;
    private final PaymentEventPublisher paymentEventPublisher;

    @DistributedLock(lockKey = "#reservationId")
    @Transactional
    public PaymentInfo.Info payment(Long reservationId, String token) {
        ConcertReservation reservation = concertReservationService.getConcertReservation(new ConcertReservationQuery.Search(reservationId));
        reservation.isReservableStatus();

        LocalDateTime paymentTime = LocalDateTime.now();
        ConcertSeat concertSeat = concertSeatService.getConcertSeat(new ConcertSeatQuery.Search(reservation.getSeatId()));
        concertSeat.checkPayableSeat(paymentTime, ConcertPolicy.TEMP_RESERVED_SEAT_MINUTES);
        concertSeat.reserveSeat(paymentTime);

        Long userId = reservation.getUserId();
        User user = userService.getUser(userId);
        UserAccount userAccount = userAccountService.getUserAccount(user.getUserId());

        long price = concertSeat.getPrice();
        userAccount.useBalance(price);

        reservation.completeReservation(paymentTime);

        paymentEventPublisher.publish(WaitingQueueEvent.ExpireTokenEvent.of(token));

        Payment payment = paymentService.createPayment(new PaymentCommand.Create(reservation.getReservationId(),
                                                                                 price,
                                                                                 PaymentStatus.APPROVED));
        return new PaymentInfo.Info(payment.getPaymentId(),
                                    payment.getReservationId(),
                                    payment.getPayAmount(),
                                    payment.getCreatedAt());
    }


}
