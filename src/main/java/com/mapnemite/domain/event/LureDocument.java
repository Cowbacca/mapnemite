package com.mapnemite.domain.event;

import lombok.Value;

@Value
public class LureDocument {
    private final double latitude;
    private final double longitude;
    private final long timePlaced;
    private final long expiryTime;
}
