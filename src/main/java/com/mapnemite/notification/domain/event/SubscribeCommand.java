package com.mapnemite.notification.domain.event;

import com.mapnemite.common.location.domain.location.Location;
import lombok.Value;

@Value
public class SubscribeCommand {
    private final String registrationId;
    private final String p256dh;
    private final String auth;
    private final Location location;
}
