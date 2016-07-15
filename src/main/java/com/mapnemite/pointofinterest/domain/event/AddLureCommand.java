package com.mapnemite.pointofinterest.domain.event;

import lombok.Value;

@Value
public class AddLureCommand {
    private final double latitude;
    private final double longitude;
}
