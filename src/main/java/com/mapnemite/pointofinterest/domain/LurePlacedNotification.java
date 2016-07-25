package com.mapnemite.pointofinterest.domain;

import com.google.common.collect.ImmutableMap;
import com.mapnemite.LocalDateTimeUtils;
import com.mapnemite.common.location.domain.location.Circle;
import com.mapnemite.notification.domain.Notification;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Map;

@Value
public class LurePlacedNotification implements Notification {
    private final Map<String, String> dataMap;
    private final Circle relevancyCircle;

    public LurePlacedNotification(Lure lure) {
        LocalDateTime expiresAt = lure.getExpiration().expiresAt();
        double latitude = lure.getLocation().getLatitude();
        double longitude = lure.getLocation().getLongitude();
        this.dataMap = ImmutableMap.<String, String>builder()
                .put("expiresAt", String.valueOf(LocalDateTimeUtils.toEpoch(expiresAt)))
                .put("latitude", String.valueOf(latitude))
                .put("longitude", String.valueOf(longitude))
                .build();

        this.relevancyCircle = new Circle(latitude, longitude, 0.5);
    }

    @Override
    public Map<String, String> asMap() {
        return dataMap;
    }

    @Override
    public Circle relevantToSubscribersWithin() {
        return relevancyCircle;
    }
}
