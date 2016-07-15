package com.mapnemite.notification.domain.event;

import lombok.Value;

@Value
public class SubscribeCommand {
    private final String registrationId;
    private final String p256dh;
    private final String auth;
}
