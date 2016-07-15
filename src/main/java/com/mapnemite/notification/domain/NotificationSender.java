package com.mapnemite.notification.domain;

public interface NotificationSender {
    void send(Notification notification, String registrationId, String publicKey, String userAuth);
}
