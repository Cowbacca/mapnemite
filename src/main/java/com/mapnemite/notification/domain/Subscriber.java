package com.mapnemite.notification.domain;

import lombok.Value;

@Value
public class Subscriber {
    private final String registrationId;
    private final String publicKey;
    private final String userAuth;


    public void sendNotification(NotificationSender notificationSender, Notification notification) {
        notificationSender.send(notification, registrationId, publicKey, userAuth);
    }
}
