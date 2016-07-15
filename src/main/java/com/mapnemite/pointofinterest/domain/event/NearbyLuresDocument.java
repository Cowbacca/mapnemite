package com.mapnemite.pointofinterest.domain.event;

import lombok.Value;

import java.util.Set;

@Value
public class NearbyLuresDocument {
    private final double latitude;
    private final double longitude;
    private final double radius;
    private final Set<LureDocument> lures;
}
