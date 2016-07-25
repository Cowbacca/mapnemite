package com.mapnemite.notification.boundary;

import com.mapnemite.common.location.domain.Location;
import com.mapnemite.notification.domain.event.SubscribeCommand;
import lombok.Data;

@Data
public class PushSubscription {
    private String endpoint;
    private Keys keys;
    private double latitude;
    private double longitude;

    public SubscribeCommand subscribeCommand() {
        return new SubscribeCommand(endpoint, keys.getP256dh(), keys.getAuth(), new Location(latitude, longitude));
    }
}
