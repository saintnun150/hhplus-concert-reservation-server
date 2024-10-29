package org.lowell.concert.interfaces.api;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.interfaces.api.common.support.ApiResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


@RestController
@RequestMapping("/api/v1/redis")
@RequiredArgsConstructor
public class RedisTestController {
    private final RedisTemplate<String, Object> template;

    @PostMapping("")
    public String setRedisValue(@RequestBody RedisRequest.Set request) {
        HashMap<Object, Object> map = new HashMap<>();
        map.put(request.getKey(), request.getValue());
        template.opsForValue().set(request.getKey(), map);
        return "success";
    }

    @GetMapping("{key}")
    public ApiResponse<?> setRedisValue(@PathVariable String key) {
        Object o = template.opsForValue()
                                      .get(key);
        return ApiResponse.createOk(o);
    }

}
