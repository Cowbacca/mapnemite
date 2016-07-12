package com.mapnemite.boundary;

import lombok.Value;

import java.util.List;
import java.util.Set;

@Value
public class NearbyPointsOfInterestDocument {
    private final double latitude;
    private final double longitude;
    private final double radius;
    private final Set<PointOfInterestDocument> pointsOfInterest;
}
