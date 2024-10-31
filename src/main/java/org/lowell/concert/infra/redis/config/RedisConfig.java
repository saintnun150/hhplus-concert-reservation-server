package org.lowell.concert.infra.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate( LettuceConnectionFactory lettuceConnectionFactory ) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory( lettuceConnectionFactory );

        // set key serializer
        template.setKeySerializer(RedisSerializer.string() );
        template.setHashKeySerializer( RedisSerializer.string() );

        // set value serializer
        template.setDefaultSerializer( RedisSerializer.json() );
        template.setValueSerializer( RedisSerializer.json() );
        template.setHashValueSerializer( RedisSerializer.json() );

        template.afterPropertiesSet();

        return template;
    }

}
