package com.mapnemite.notification.domain.subscriber;

import com.mapnemite.common.location.domain.Location;
import com.mapnemite.notification.domain.Notification;
import com.mapnemite.notification.domain.NotificationSender;
import lombok.Value;

@Value
public class Subscriber {
    private final String registrationId;
    private final String publicKey;
    private final String userAuth;
    private final Location lastKnownPosition;
    private final boolean hasUnreadNotification;


    public void sendNotification(NotificationSender notificationSender, Notification notification, SubscriberRepository subscriberRepository) {
        if (!hasUnreadNotification) {
            notificationSender.send(notification, registrationId, publicKey, userAuth);
            subscriberRepository.save(new Subscriber(registrationId, publicKey, userAuth, lastKnownPosition, true));
        }
    }
}
