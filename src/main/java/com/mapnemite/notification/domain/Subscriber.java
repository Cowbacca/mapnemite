package com.mapnemite.notification.domain;

import lombok.Value;

@Value
public class Subscriber {
    private final String registrationId;

    public void sendNotification(NotificationSender notificationSender) {
        notificationSender.send(registrationId);
    }
}
