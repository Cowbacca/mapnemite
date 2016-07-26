package com.mapnemite.notification.domain;

import com.mapnemite.notification.domain.subscriber.SubscriberRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class NotificationToSubscribersPusher {
    private final SubscriberRepository subscriberRepository;
    private final NotificationSender notificationSender;

    @Inject
    public NotificationToSubscribersPusher(SubscriberRepository subscriberRepository, NotificationSender notificationSender) {
        this.subscriberRepository = subscriberRepository;
        this.notificationSender = notificationSender;
    }

    public void pushNotificationToSubscribers(Notification notification) {
        subscriberRepository.findByLocationWithin(notification.relevantToSubscribersWithin())
                .forEach(subscriber -> subscriber.sendNotification(notificationSender, notification, subscriberRepository));
    }
}
