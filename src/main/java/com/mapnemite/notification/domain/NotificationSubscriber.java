package com.mapnemite.notification.domain;

import com.mapnemite.notification.domain.event.SubscribeCommand;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class NotificationSubscriber {
    private final SubscriberRepository subscriberRepository;

    @Inject
    public NotificationSubscriber(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    public void subscribe(SubscribeCommand subscribeCommand) {
        subscriberRepository.save(new Subscriber(subscribeCommand.getRegistrationId()));
    }
}
