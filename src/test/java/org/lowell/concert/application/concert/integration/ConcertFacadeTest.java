package org.lowell.concert.application.concert.integration;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lowell.concert.application.concert.ConcertFacade;
import org.lowell.concert.application.support.DatabaseCleanUp;
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

@Slf4j
@SpringBootTest
public class ConcertFacadeTest {

    @Autowired
    private ConcertFacade concertFacade;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private ConcertSeatJpaRepository concertSeatJpaRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.execute();
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

        List<ConcertSeat> all = concertSeatJpaRepository.findAll();
        all.stream().forEach(seat -> log.info("## seatID : {}", seat.getSeatId()));


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

    @DisplayName("특정 좌석을 동시에 100번 예약 진행할 경우 한 번의 요청만 좌석 예약에 성공한다.")
    @Test
    void reserveConcertSeatConcurrently100Times() throws InterruptedException {
        // given
        User u1 = User.builder()
                      .userId(1L)
                      .username("user1")
                      .username("user1")
                      .build();

        int reserveCount = 3;
        userJpaRepository.saveAll(List.of(u1));
        concertSeatJpaRepository.saveAll(List.of(new ConcertSeat(1L, 1L, 1, SeatStatus.EMPTY, 1000, null, null),
                                                 new ConcertSeat(2L, 1L, 2, SeatStatus.EMPTY, 1000, null, null)));

        ExecutorService executorService = Executors.newFixedThreadPool(reserveCount);
        CountDownLatch latch = new CountDownLatch(reserveCount);
        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger failed = new AtomicInteger(0);
        for (int i = 0; i < reserveCount; i++) {
            executorService.submit(() -> {
                attemptReservation(concertFacade, 1L, u1.getUserId(), success, failed, latch);
            });
        }
        latch.await();
        executorService.shutdown();

        assertEquals(1, success.get());
        assertEquals(reserveCount - 1, failed.get());
    }

    private void attemptReservation(ConcertFacade concertFacade, Long seatId, Long userId, AtomicInteger success, AtomicInteger failed, CountDownLatch latch) {
        try {
            concertFacade.reserveConcertSeat(seatId, userId);
            success.incrementAndGet();
        } catch (Exception e) {
            log.error("## error : {}", e.getClass().getSimpleName());
            failed.incrementAndGet();
        } finally {
            latch.countDown();
        }
    }
}
