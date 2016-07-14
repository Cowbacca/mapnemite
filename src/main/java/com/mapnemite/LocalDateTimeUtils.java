package com.mapnemite;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class LocalDateTimeUtils {
    public static long toEpoch(LocalDateTime time) {
        ZoneId zoneId = ZoneId.systemDefault();
        return time.atZone(zoneId).toInstant().toEpochMilli();
    }

    public static LocalDateTime fromEpoch(long seconds) {
        return Instant.ofEpochMilli(seconds)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
