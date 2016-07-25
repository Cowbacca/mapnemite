package com.mapnemite.pointofinterest.domain;

import com.mapnemite.common.location.domain.Location;
import lombok.Value;

import java.time.Duration;
import java.time.LocalDateTime;

@Value
public class Lure implements Expirable {
    private Location location;
    private Expiration expiration;

    public Lure(Location location, LocalDateTime placedAt) {
        this.location = location;
        this.expiration = new Expiration(placedAt, Duration.ofMinutes(30));
    }

    public boolean isExpired() {
        return expiration.isExpiredAt(LocalDateTime.now());
    }
}
