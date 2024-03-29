package com.mapnemite.notification.boundary;

import com.mapnemite.common.location.boundary.GeoPoint;
import com.mapnemite.common.location.domain.Location;
import com.mapnemite.notification.domain.subscriber.Subscriber;
import lombok.Value;

@Value
public class IndexedSubscriber {
    private final String registrationId;
    private final String publicKey;
    private final String userAuth;
    private final GeoPoint lastKnownLocation;
    private final boolean hasUnreadNotification;

    public IndexedSubscriber(Subscriber subscriber) {
        this.registrationId = subscriber.getRegistrationId();
        this.publicKey = subscriber.getPublicKey();
        this.userAuth = subscriber.getUserAuth();
        Location lastKnownPosition = subscriber.getLastKnownPosition();
        this.lastKnownLocation = new GeoPoint(lastKnownPosition.getLatitude(), lastKnownPosition.getLongitude());
        this.hasUnreadNotification = subscriber.isHasUnreadNotification();
    }

    public Subscriber toSubscriber() {
        return new Subscriber(registrationId, publicKey, userAuth, new Location(lastKnownLocation.getLat(), lastKnownLocation.getLon()), hasUnreadNotification);
    }
}
