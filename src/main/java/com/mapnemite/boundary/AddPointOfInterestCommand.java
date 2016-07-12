package com.mapnemite.boundary;

import lombok.Value;

@Value
public class AddPointOfInterestCommand {
    private final double latitude;
    private final double longitude;
    private final String type;
}
