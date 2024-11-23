package org.lowell.apps.redis.infra;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lowell.apps.support.DatabaseCleanUp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class RedisDataStructureTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.cleanRedisData();
    }

    @DisplayName("Redis sorted set 저장 테스트")
    @Test
    void setRedisSortedSet() {
        // given
        String key = "test";

        // when
        for (int i = 1; i <= 10; i++) {
            redisTemplate.opsForZSet().add(key, StringUtils.replace(UUID.randomUUID().toString(), "\"", " "), i);
        }

        // then
        assertThat(redisTemplate.opsForZSet().size(key)).isEqualTo(10);
    }

    @DisplayName("Redis sorted set 삭제 테스트")
    @Test
    void deleteRedisSortedSet() {
        // given
        String key = "test";
        String value = "test22";
        // when
        redisTemplate.opsForZSet()
                     .add(key, value, 1);
        redisTemplate.opsForZSet()
                     .remove(key, value);
        // then
        assertThat(redisTemplate.opsForZSet().score(key, value)).isNull();
    }

    @DisplayName("test라는 key의 sorted set에 value 존재 여부 및 순서 확인")
    @Test
    void checkRedisSortedSetAndRank() throws InterruptedException {
        // given
        String key = "test";

        List<String> values = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String value = UUID.randomUUID().toString();
            values.add(value);
            long score = System.currentTimeMillis();
            Thread.sleep(1);
            redisTemplate.opsForZSet().add(key, value, score);
        }
        // when
        Long rank = redisTemplate.opsForZSet()
                                 .rank(key, values.get(2));

        Boolean isMember = rank != null;
        // then
        assertThat(rank).isNotNull().isEqualTo(2L);
        assertThat(isMember).isTrue();
    }

    @DisplayName("test라는 key의 sorted set에 0부터 1까지의 순서에 해당하는 value를 꺼내와서 각각 String 자료구조에 만료 시간은 10분으로 redis에 저장")
    @Test
    void saveAndRemoveTop4ValuesFromSortedSetAsStringWithExpiry() throws InterruptedException {
        // given
        String key = "test";
        // when
        List<String> values = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String value = UUID.randomUUID().toString();
            values.add(value);
            long score = System.currentTimeMillis();
            Thread.sleep(1);
            redisTemplate.opsForZSet().add(key, value, score);
        }

        Set<Object> range = redisTemplate.opsForZSet()
                                         .range(key, 0L, 1L);

        for (Object value : range) {
            redisTemplate.opsForValue().set(value.toString(), value.toString(), 10L, TimeUnit.MINUTES);
            redisTemplate.opsForZSet().remove(key, value);
        }
        // then
        assertThat(redisTemplate.opsForZSet().zCard(key)).isEqualTo(8);
        assertThat(redisTemplate.opsForValue().get(values.get(0))).isNotNull();
        assertThat(redisTemplate.opsForValue().get(values.get(1))).isNotNull();
    }

}
