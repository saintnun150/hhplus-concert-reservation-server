package org.lowell.apps.waitingqueue.infra.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisRepository {
    private final RedisTemplate<String, Object> redisTemplate;

    public Boolean addIfAbsent(String key, String value, Long leaseTime, TimeUnit timeUnit){
        return redisTemplate.opsForValue().setIfAbsent(key, value, leaseTime, timeUnit);
    }

    public Boolean deleteKey(String key){
        return redisTemplate.delete(key);
    }
    public Boolean existsKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public Boolean addZSet(String key, String value, double score){
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    public Boolean addZSetIfAbsent(String key, String value, double score){
        return redisTemplate.opsForZSet().addIfAbsent(key, value, score);
    }

    public ZSetOperations<String, Object> getZSetOps() {
        return redisTemplate.opsForZSet();
    }

    public Set<Object> getZSetRange(String key, long start, long end) {
        return this.getZSetOps().range(key, start, end);
    }

    public Long getZSetRank(String key, Object value) {
        return this.getZSetOps().rank(key, value);
    }

    public Long getZSetSize(String key) {
        return this.getZSetOps().zCard(key);
    }

    public Set<ZSetOperations.TypedTuple<Object>> zPopMin(String key, long count) {
        return this.getZSetOps().popMin(key, count);
    }

    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Double getZScore(String key, Object value) {
        return redisTemplate.opsForZSet().score(key, value);
    }
}
