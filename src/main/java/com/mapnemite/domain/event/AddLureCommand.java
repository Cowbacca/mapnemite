package com.mapnemite.domain.event;

import lombok.Value;

@Value
public class AddLureCommand {
    private final double latitude;
    private final double longitude;
}
