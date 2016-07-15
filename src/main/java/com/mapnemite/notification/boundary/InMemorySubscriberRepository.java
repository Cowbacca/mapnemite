package com.mapnemite.notification.boundary;

import com.mapnemite.notification.domain.Subscriber;
import com.mapnemite.notification.domain.SubscriberRepository;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@Component
public class InMemorySubscriberRepository implements SubscriberRepository {
    private final Set<Subscriber> subscribers;

    public InMemorySubscriberRepository() {
        subscribers = new HashSet<>();
    }

    @Override
    public void save(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public Stream<Subscriber> findAll() {
        return subscribers.stream();
    }
}
