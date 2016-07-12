package com.mapnemite.domain;

import com.mapnemite.domain.location.Location;
import lombok.Value;

@Value
public class PointOfInterest {
    private Location location;
    private Type type;
}
