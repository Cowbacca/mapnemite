package com.mapnemite.boundary;

import lombok.Value;

@Value
public class PointOfInterestDocument {
    private final String type;
    private final double latitude;
    private final double longitude;
}
