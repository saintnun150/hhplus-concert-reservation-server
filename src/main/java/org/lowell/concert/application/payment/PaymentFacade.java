package org.lowell.concert.application.payment;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.concert.ConcertPolicy;
import org.lowell.concert.domain.concert.dto.ConcertReservationQuery;
import org.lowell.concert.domain.concert.dto.ConcertSeatQuery;
import org.lowell.concert.domain.concert.model.ConcertReservation;
import org.lowell.concert.domain.concert.model.ConcertSeat;
import org.lowell.concert.domain.concert.service.ConcertReservationService;
import org.lowell.concert.domain.concert.service.ConcertSeatService;
import org.lowell.concert.domain.payment.dto.PaymentCommand;
import org.lowell.concert.domain.payment.model.PaymentStatus;
import org.lowell.concert.domain.payment.service.PaymentService;
import org.lowell.concert.domain.user.model.User;
import org.lowell.concert.domain.user.model.UserAccount;
import org.lowell.concert.domain.user.service.UserAccountService;
import org.lowell.concert.domain.user.service.UserService;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueQuery;
import org.lowell.concert.domain.waitingqueue.model.WaitingQueue;
import org.lowell.concert.domain.waitingqueue.service.WaitingQueueService;
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
    private final WaitingQueueService waitingQueueService;

    @Transactional
    public void payment(Long reservationId, String token) {
        ConcertReservation reservation = concertReservationService.getConcertReservation(new ConcertReservationQuery.Search(reservationId));
        reservation.isReservableStatus();
        ConcertSeat concertSeat = concertSeatService.getConcertSeatWithLock(new ConcertSeatQuery.Search(reservation.getSeatId()));

        LocalDateTime paymentTime = LocalDateTime.now();
        concertSeat.checkAvailableSeat(paymentTime, ConcertPolicy.TEMP_RESERVED_SEAT_MINUTES);

        Long userId = reservation.getUserId();
        User user = userService.getUser(userId);
        UserAccount userAccount = userAccountService.getUserAccountWithLock(user.getUserId());

        long price = concertSeat.getPrice();
        userAccount.useBalance(price);

        concertSeat.reserveSeat(paymentTime);
        reservation.completeReservation(paymentTime);

        WaitingQueue waitingQueue = waitingQueueService.getWaitingQueue(new WaitingQueueQuery.GetQueue(token));
        waitingQueue.expiredToken(paymentTime, ConcertPolicy.EXPIRED_QUEUE_MINUTES);

        paymentService.createPayment(new PaymentCommand.Create(reservation.getReservationId(), price, PaymentStatus.APPROVED));

    }


}
