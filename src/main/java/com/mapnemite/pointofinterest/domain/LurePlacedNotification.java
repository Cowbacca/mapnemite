package com.mapnemite.pointofinterest.domain;

import com.google.common.collect.ImmutableMap;
import com.mapnemite.LocalDateTimeUtils;
import com.mapnemite.notification.domain.Notification;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Map;

@Value
public class LurePlacedNotification implements Notification {
    private final LocalDateTime expiresAt;

    @Override
    public Map<String, String> asMap() {
        return ImmutableMap.<String, String>builder()
                .put("expiresAt", String.valueOf(LocalDateTimeUtils.toEpoch(expiresAt)))
                .build();
    }
}
