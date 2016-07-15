package com.mapnemite.notification.domain;

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
        subscriberRepository.findAll()
                .forEach(subscriber -> subscriber.sendNotification(notificationSender, notification));
    }
}
