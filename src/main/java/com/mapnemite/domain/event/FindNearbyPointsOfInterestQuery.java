package com.mapnemite.domain.event;

import lombok.Value;

@Value
public class FindNearbyPointsOfInterestQuery {
    private final double latitude;
    private final double longitude;
    private final double radius;
}
