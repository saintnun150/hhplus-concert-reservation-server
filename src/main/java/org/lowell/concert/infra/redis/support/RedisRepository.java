package org.lowell.concert.infra.redis.support;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisRepository{
    private final RedisTemplate<String, Object> redisTemplate;

    public Boolean setIfAbsent(String key, String value, Long leaseTime, TimeUnit timeUnit){
        return redisTemplate.opsForValue().setIfAbsent(key, value, leaseTime, timeUnit);
    }

    public Boolean delete(String key){
        return redisTemplate.delete(key);
    }
}
