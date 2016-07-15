package com.mapnemite.notification.domain.event;

import lombok.Value;

@Value
public class SubscribeCommand {
    private final String registrationId;
}
