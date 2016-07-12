package com.mapnemite.external;

import com.mapnemite.boundary.AddPointOfInterestCommand;
import com.mapnemite.boundary.FindNearbyPointsOfInterestQuery;
import com.mapnemite.boundary.NearbyPointsOfInterestDocument;
import com.mapnemite.domain.PointOfInterestAdder;
import com.mapnemite.domain.PointOfInterestFinder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@Controller
@RequestMapping("/pointsOfInterest")
@CrossOrigin
public class PointOfInterestController {

    private final PointOfInterestFinder pointOfInterestFinder;
    private final PointOfInterestAdder pointOfInterestAdder;

    @Inject
    public PointOfInterestController(PointOfInterestFinder pointOfInterestFinder, PointOfInterestAdder pointOfInterestAdder) {
        this.pointOfInterestFinder = pointOfInterestFinder;
        this.pointOfInterestAdder = pointOfInterestAdder;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    private NearbyPointsOfInterestDocument getNearby(@RequestParam double latitude, @RequestParam double longitude, @RequestParam double radius) {
        FindNearbyPointsOfInterestQuery query = new FindNearbyPointsOfInterestQuery(latitude, longitude, radius);
        return pointOfInterestFinder.findNearbyPointsOfInterest(query);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    private void putPointOfInterest(@RequestParam double latitude, @RequestParam double longitude, @RequestParam String type) {
        AddPointOfInterestCommand command = new AddPointOfInterestCommand(latitude, longitude, type.toUpperCase());
        pointOfInterestAdder.addPointOfInterest(command);
    }
}
