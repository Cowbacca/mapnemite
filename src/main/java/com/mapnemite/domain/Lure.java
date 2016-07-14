package com.mapnemite.domain;

import com.mapnemite.domain.location.Location;
import lombok.Value;

import java.time.Duration;
import java.time.LocalDateTime;

@Value
public class Lure implements Expirable {
    private Location location;
    private Expiration expiration;

    public Lure(Location location, LocalDateTime timePlaced) {
        this.location = location;
        this.expiration = new Expiration(timePlaced, Duration.ofMinutes(30));
    }

    public boolean isExpired() {
        return expiration.isExpiredAt(LocalDateTime.now());
    }
}
