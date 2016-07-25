package com.mapnemite.common.location.domain;

import lombok.Value;

@Value
public class Rectangle {
    private final Location topRight;
    private final Location bottomLeft;
}
