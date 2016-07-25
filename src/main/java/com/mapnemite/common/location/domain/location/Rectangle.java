package com.mapnemite.common.location.domain.location;

import lombok.Value;

@Value
public class Rectangle {
    private final Location topRight;
    private final Location bottomLeft;
}
