package org.lowell.concert.domain.concert.integration;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lowell.concert.domain.concert.dto.ConcertScheduleCommand;
import org.lowell.concert.domain.concert.dto.ConcertScheduleQuery;
import org.lowell.concert.domain.concert.repository.ConcertScheduleRepository;
import org.lowell.concert.domain.concert.service.ConcertScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@ActiveProfiles("local")
@SpringBootTest
public class ConcertScheduleCacheTest {
    @Autowired
    private ConcertScheduleRepository concertScheduleRepository;

    @Autowired
    private ConcertScheduleService concertScheduleService;

    @DisplayName("콘서트 스케쥴 천 개 생성하는 코드")
    @Test
    void createConcertSchedule() {
        // given
        int count = 1000;
        LocalDateTime date = LocalDateTime.now();
        LocalDateTime start = date.plusHours(10);
        LocalDateTime end = start.plusHours(2);

        // when
        for (int i = 0; i < count; i++) {
            concertScheduleRepository.createConcertSchedule(
                    new ConcertScheduleCommand.Create((long) i, date, start, end)
            );
        }
    }

    @DisplayName("콘서트 스케쥴이 1000개에 대한 db 조회 속도 확인")
    @Test
    void getConcertSchedule() throws InterruptedException {
        // given
        int threadCount = 100;
        long scheduleId = 1;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        String taskName = "100명의 동시 요청으로 1000개 콘서트 스케줄 조회 성능 테스트";
        StopWatch stopWatch = new StopWatch(String.format(taskName, threadCount));
        stopWatch.start(taskName);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                for (int j = 0; j < threadCount; j++) {
                    try {
                        concertScheduleService.getConcertSchedules(new ConcertScheduleQuery.SearchList(scheduleId, null, null));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } finally {
                        latch.countDown();
                    }
                }
            });
        }
        latch.await();
        executorService.shutdown();
        if (executorService.awaitTermination(100, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }

        stopWatch.stop();
        log.info(stopWatch.prettyPrint());

    }

    @DisplayName("콘서트 스케쥴이 1000개에 대한 redis 캐시 조회 속도 확인")
    @Test
    void getConcertScheduleForRedis() throws InterruptedException {
        // given
        int threadCount = 100;
        long scheduleId = 1;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        String taskName = "100명의 동시 요청으로 1000개 콘서트 스케줄 조회 성능 테스트 For Redis";
        StopWatch stopWatch = new StopWatch(String.format(taskName, threadCount));
        stopWatch.start(taskName);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                for (int j = 0; j < threadCount; j++) {
                    try {
                        concertScheduleService.getConcertSchedulesWithCache(new ConcertScheduleQuery.SearchList(scheduleId, null, null));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } finally {
                        latch.countDown();
                    }
                }
            });
        }
        latch.await();
        executorService.shutdown();
        if (executorService.awaitTermination(100, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }

        stopWatch.stop();
        log.info(stopWatch.prettyPrint());

    }

}
