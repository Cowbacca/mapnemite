package com.mapnemite.domain.location;

import lombok.Value;

@Value
public class Circle {
    private final double x;
    private final double y;
    private final double radius;
}
