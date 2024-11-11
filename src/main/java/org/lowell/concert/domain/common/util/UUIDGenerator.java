package org.lowell.concert.domain.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class UUIDGenerator {
    public static String generateTimestampUUID() {
        long timestamp = System.currentTimeMillis();
        String uniqueId = UUID.randomUUID().toString().replace("-", "").substring(0, 17);
        return timestamp + uniqueId;
    }
}
