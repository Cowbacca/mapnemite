package com.mapnemite.boundary;

import com.mapnemite.domain.Lure;
import com.mapnemite.domain.location.Location;
import lombok.Value;

import static com.mapnemite.LocalDateTimeUtils.fromEpoch;
import static com.mapnemite.LocalDateTimeUtils.toEpoch;

@Value
public class IndexedLure {
    private final GeoPoint location;
    private final long placedAt;
    private final long expiresAt;

    public IndexedLure(Lure lure) {
        double latitude = lure.getLocation().getLatitude();
        double longitude = lure.getLocation().getLongitude();
        this.location =  new GeoPoint(latitude, longitude);
        this.placedAt = toEpoch(lure.getExpiration().getPlacedAt());
        this.expiresAt = toEpoch(lure.getExpiration().expiresAt());
    }

    public Lure toPointOfInterest() {
        return new Lure(new Location(location.getLat(), location.getLon()), fromEpoch(placedAt));
    }
}
