package org.lowell.concert.domain.concert;

import java.util.concurrent.TimeUnit;

public class ConcertPolicy {
    public static final Long ACTIVATE_QUEUE_INTERVAL = 5L;
    public static final TimeUnit ACTIVATE_QUEUE_TIME_UNIT = TimeUnit.SECONDS;
    public static final int ACTIVATE_QUEUE_CAPACITY = 50;
    public static final Long ACTIVATE_QUEUE_SIZE = 50L;
    public static int TEMP_RESERVED_SEAT_MINUTES = 5;
    public static long EXPIRED_QUEUE_MINUTES = 10;
}
