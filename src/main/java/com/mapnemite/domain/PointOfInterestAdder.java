package com.mapnemite.domain;

import com.mapnemite.boundary.AddPointOfInterestCommand;
import com.mapnemite.boundary.PointOfInterestRepository;
import com.mapnemite.domain.location.Location;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class PointOfInterestAdder {

    private final PointOfInterestRepository pointOfInterestRepository;

    @Inject
    public PointOfInterestAdder(PointOfInterestRepository pointOfInterestRepository) {
        this.pointOfInterestRepository = pointOfInterestRepository;
    }

    public void addPointOfInterest(AddPointOfInterestCommand addPointOfInterestCommand) {
        Location location = new Location(addPointOfInterestCommand.getLatitude(), addPointOfInterestCommand.getLongitude());
        Type type = Type.valueOf(addPointOfInterestCommand.getType());
        PointOfInterest pointOfInterest = new PointOfInterest(location, type);

        pointOfInterestRepository.save(pointOfInterest);
    }
}
