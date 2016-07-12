package com.mapnemite.boundary;

import com.mapnemite.domain.location.Circle;
import com.mapnemite.domain.PointOfInterest;

import java.util.Set;

public interface PointOfInterestRepository {
    void save(PointOfInterest pointOfInterest);

    Set<PointOfInterest> findByLocationWithin(Circle circle);
}
