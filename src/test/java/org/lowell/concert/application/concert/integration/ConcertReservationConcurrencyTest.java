package org.lowell.concert.application.concert.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lowell.concert.application.concert.ConcertFacade;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.concert.exception.ConcertSeatError;
import org.lowell.concert.domain.concert.model.ConcertSeat;
import org.lowell.concert.domain.concert.model.SeatStatus;
import org.lowell.concert.domain.user.model.User;
import org.lowell.concert.infra.db.concert.repository.ConcertSeatJpaRepository;
import org.lowell.concert.infra.db.user.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ConcertReservationConcurrencyTest {

    @Autowired
    private ConcertFacade concertFacade;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private ConcertSeatJpaRepository concertSeatJpaRepository;

    @BeforeEach
    void setUp() {
        userJpaRepository.deleteAll();
        concertSeatJpaRepository.deleteAll();
    }

    @DisplayName("특정 좌석을 동시에 예약 진행할 경우 좌석당 한 건의 예약만 성공한다.")
    @Test
    void reserveConcertSeatConcurrently() throws InterruptedException {
        // given
        User u1 = User.builder()
                      .userId(1L)
                      .username("user1")
                      .username("user1")
                      .build();
        User u2 = User.builder()
                      .userId(2L)
                      .username("user1")
                      .username("user1")
                      .build();
        User u3 = User.builder()
                      .userId(3L)
                      .username("user1")
                      .username("user1")
                      .build();
        User u4 = User.builder()
                      .userId(3L)
                      .username("user1")
                      .username("user1")
                      .build();

        int reserveCount = 4;

        userJpaRepository.saveAll(List.of(u1, u2, u3, u4));

        concertSeatJpaRepository.saveAll(List.of(new ConcertSeat(1L, 1L, 1, SeatStatus.EMPTY, 1000, null, null),
                                                 new ConcertSeat(2L, 1L, 2, SeatStatus.EMPTY, 1000, null, null)));

        ExecutorService executorService = Executors.newFixedThreadPool(reserveCount);
        CountDownLatch latch = new CountDownLatch(reserveCount);

        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger failed = new AtomicInteger(0);


        List<Runnable> reservationTasks = List.of(
                () -> attemptReservation(concertFacade, 1L, u1.getUserId(), success, failed, latch),
                () -> attemptReservation(concertFacade, 2L, u2.getUserId(), success, failed, latch),
                () -> attemptReservation(concertFacade, 1L, u3.getUserId(), success, failed, latch),
                () -> attemptReservation(concertFacade, 2L, u4.getUserId(), success, failed, latch)
        );
        for (Runnable task : reservationTasks) {
            executorService.submit(task);
        }
        latch.await();
        executorService.shutdown();

        assertEquals(2, success.get());
        assertEquals(2, failed.get());
    }

    private void attemptReservation(ConcertFacade concertFacade, Long seatId, Long userId, AtomicInteger success, AtomicInteger failed, CountDownLatch latch) {
        try {
            concertFacade.reserveConcertSeat(seatId, userId);
            success.incrementAndGet();
        } catch (DomainException e) {
            if (e.getDomainError() == ConcertSeatError.RESERVED_TEMPORARY) {
                System.out.println("## 임시 배정 상태");
                failed.incrementAndGet();
            }
        } finally {
            latch.countDown();
        }
    }
}
