package com.mapnemite.external;

import com.mapnemite.domain.PointOfInterest;
import com.mapnemite.domain.Type;
import com.mapnemite.domain.location.Location;
import lombok.Value;

@Value
public class IndexedPointOfInterest {
    private final GeoPoint location;
    private final String type;

    public IndexedPointOfInterest(PointOfInterest pointOfInterest) {
        double latitude = pointOfInterest.getLocation().getLatitude();
        double longitude = pointOfInterest.getLocation().getLongitude();
        this.location =  new GeoPoint(latitude, longitude);
        this.type = pointOfInterest.getType().toString();
    }

    public PointOfInterest toPointOfInterest() {
        return new PointOfInterest(new Location(location.getLat(), location.getLon()), Type.valueOf(type.toUpperCase()));
    }
}
