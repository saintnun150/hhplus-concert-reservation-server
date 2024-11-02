package org.lowell.concert.infra.redis.support;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisRepository{
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedissonClient redissonClient;

    public Boolean setIfAbsent(String key, String value, Long leaseTime, TimeUnit timeUnit){
        return redisTemplate.opsForValue().setIfAbsent(key, value, leaseTime, timeUnit);
    }

    public Boolean delete(String key){
        return redisTemplate.delete(key);
    }

    public RLock getLock(String key){
        return redissonClient.getLock(key);
    }

    public void unlock(String key){
        RLock lock = redissonClient.getLock(key);
        lock.unlock();
    }
}
