package com.mapnemite.domain;

import lombok.Value;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;

@Value
public class Expiration {
    private LocalDateTime timePlaced;
    private TemporalAmount timeToLive;

    public boolean isExpiredAt(LocalDateTime time) {
        return time.isAfter(expiresAt());
    }

    public LocalDateTime expiresAt() {
        return timePlaced.plus(timeToLive);
    }
}
