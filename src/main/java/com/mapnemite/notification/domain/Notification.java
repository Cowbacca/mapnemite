package com.mapnemite.notification.domain;

import com.mapnemite.common.location.domain.location.Circle;

import java.util.Map;

public interface Notification {
    Map<String, String> asMap();

    Circle relevantToSubscribersWithin();
}
