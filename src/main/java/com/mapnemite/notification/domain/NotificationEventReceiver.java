package com.mapnemite.notification.domain;

import com.mapnemite.notification.domain.event.SendNotificationCommand;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class NotificationEventReceiver {
    private final SubscriberRepository subscriberRepository;
    private final NotificationSender notificationSender;

    @Inject
    public NotificationEventReceiver(SubscriberRepository subscriberRepository, NotificationSender notificationSender) {
        this.subscriberRepository = subscriberRepository;
        this.notificationSender = notificationSender;
    }

    public void send(SendNotificationCommand sendNotificationCommand) {
        subscriberRepository.findAll()
                .forEach(subscriber -> subscriber.sendNotification(notificationSender));
    }
}
