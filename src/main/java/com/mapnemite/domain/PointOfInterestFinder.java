package com.mapnemite.domain;

import com.mapnemite.boundary.FindNearbyPointsOfInterestQuery;
import com.mapnemite.boundary.NearbyPointsOfInterestDocument;
import com.mapnemite.boundary.PointOfInterestDocument;
import com.mapnemite.boundary.PointOfInterestRepository;
import com.mapnemite.domain.location.Circle;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Component
public class PointOfInterestFinder {
    private final PointOfInterestRepository pointOfInterestRepository;

    @Inject
    public PointOfInterestFinder(PointOfInterestRepository pointOfInterestRepository) {
        this.pointOfInterestRepository = pointOfInterestRepository;
    }

    public NearbyPointsOfInterestDocument findNearbyPointsOfInterest(FindNearbyPointsOfInterestQuery findNearbyPointsOfInterestQuery) {
        final double latitude = findNearbyPointsOfInterestQuery.getLatitude();
        final double longitude = findNearbyPointsOfInterestQuery.getLongitude();
        final double radius = findNearbyPointsOfInterestQuery.getRadius();

        Circle circle = new Circle(latitude, longitude, radius);
        Set<PointOfInterest> pointsOfInterest = pointOfInterestRepository.findByLocationWithin(circle);
        return new NearbyPointsOfInterestDocument(latitude, longitude, radius, pointOfInterestDocuments(pointsOfInterest));
    }

    public static Set<PointOfInterestDocument> pointOfInterestDocuments(Set<PointOfInterest> pointsOfInterest) {
        return pointsOfInterest.stream()
                .map(PointOfInterestFinder::pointOfInterest)
                .collect(toSet());
    }

    public static PointOfInterestDocument pointOfInterest(PointOfInterest pointOfInterest) {
        final String type = pointOfInterest.getType().toString();
        final double latitude = pointOfInterest.getLocation().getLatitude();
        final double longitude = pointOfInterest.getLocation().getLongitude();
        return new PointOfInterestDocument(type, latitude, longitude);
    }
}
