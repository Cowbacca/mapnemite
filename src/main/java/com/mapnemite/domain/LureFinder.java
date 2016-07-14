package com.mapnemite.domain;

import com.mapnemite.domain.event.FindNearbyPointsOfInterestQuery;
import com.mapnemite.domain.event.LureDocument;
import com.mapnemite.domain.event.NearbyLuresDocument;
import com.mapnemite.domain.location.Circle;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Set;

import static com.mapnemite.LocalDateTimeUtils.toEpoch;
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
        Set<Lure> lures = lureRepository.findByLocationWithin(circle);
        return new NearbyLuresDocument(latitude, longitude, radius, lureDocuments(lures));
    }

    public static Set<LureDocument> lureDocuments(Set<Lure> lures) {
        return lures.stream()
                .map(LureFinder::pointOfInterest)
                .collect(toSet());
    }

    public static LureDocument pointOfInterest(Lure pointOfInterest) {
        final double latitude = pointOfInterest.getLocation().getLatitude();
        final double longitude = pointOfInterest.getLocation().getLongitude();
        final long timePlaced = toEpoch(pointOfInterest.getExpiration().getTimePlaced());
        final long expiryTime = toEpoch(pointOfInterest.getExpiration().expiresAt());
        return new LureDocument(latitude, longitude, timePlaced, expiryTime);
    }
}
