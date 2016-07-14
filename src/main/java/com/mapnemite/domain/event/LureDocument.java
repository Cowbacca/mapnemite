package com.mapnemite.domain.event;

import com.mapnemite.domain.Lure;
import lombok.Value;

import static com.mapnemite.LocalDateTimeUtils.toEpoch;

@Value
public class LureDocument {
    private final double latitude;
    private final double longitude;
    private final long placedAt;
    private final long expiresAt;

    public LureDocument(Lure lure) {
        this.latitude = lure.getLocation().getLatitude();
        this.longitude = lure.getLocation().getLongitude();
        this.placedAt = toEpoch(lure.getExpiration().getPlacedAt());
        this.expiresAt = toEpoch(lure.getExpiration().expiresAt());
    }
}
