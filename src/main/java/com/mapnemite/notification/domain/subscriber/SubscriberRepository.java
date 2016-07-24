package com.mapnemite.notification.domain.subscriber;

import java.util.stream.Stream;

public interface SubscriberRepository {
    void save(Subscriber subscriber);

    Stream<Subscriber> findAll();
}
