package com.mapnemite.notification.domain.subscriber;

import com.mapnemite.common.location.domain.location.Circle;

import java.util.stream.Stream;

public interface SubscriberRepository {
    void save(Subscriber subscriber);

    Stream<Subscriber> findByLocationWithin(Circle circle);
}
