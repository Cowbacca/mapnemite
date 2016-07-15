package com.mapnemite.pointofinterest.domain;

import com.mapnemite.pointofinterest.domain.event.FindNearbyPointsOfInterestQuery;
import com.mapnemite.pointofinterest.domain.event.LureDocument;
import com.mapnemite.pointofinterest.domain.event.NearbyLuresDocument;
import com.mapnemite.pointofinterest.domain.location.Circle;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Component
public class LureFinder {
    private final LureRepository lureRepository;

    @Inject
    public LureFinder(LureRepository lureRepository) {
        this.lureRepository = lureRepository;
    }

    public NearbyLuresDocument findNearbyPointsOfInterest(FindNearbyPointsOfInterestQuery findNearbyPointsOfInterestQuery) {
        final double latitude = findNearbyPointsOfInterestQuery.getLatitude();
        final double longitude = findNearbyPointsOfInterestQuery.getLongitude();
        final double radius = findNearbyPointsOfInterestQuery.getRadius();

        Circle circle = new Circle(latitude, longitude, radius);
        Set<Lure> lures = lureRepository.findByLocationWithinAndNotExpired(circle);
        return new NearbyLuresDocument(latitude, longitude, radius, lureDocuments(lures));
    }

    public static Set<LureDocument> lureDocuments(Set<Lure> lures) {
        return lures.stream()
                .map(LureDocument::new)
                .collect(toSet());
    }
}
