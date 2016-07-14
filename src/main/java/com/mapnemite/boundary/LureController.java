package com.mapnemite.boundary;

import com.mapnemite.domain.LureAdder;
import com.mapnemite.domain.LureFinder;
import com.mapnemite.domain.event.AddLureCommand;
import com.mapnemite.domain.event.FindNearbyPointsOfInterestQuery;
import com.mapnemite.domain.event.NearbyLuresDocument;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@Controller
@RequestMapping("/lures")
@CrossOrigin
public class LureController {

    private final LureFinder lureFinder;
    private final LureAdder lureAdder;

    @Inject
    public LureController(LureFinder lureFinder, LureAdder lureAdder) {
        this.lureFinder = lureFinder;
        this.lureAdder = lureAdder;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    private NearbyLuresDocument getNearby(@RequestParam double latitude, @RequestParam double longitude, @RequestParam double radius) {
        FindNearbyPointsOfInterestQuery query = new FindNearbyPointsOfInterestQuery(latitude, longitude, radius);
        return lureFinder.findNearbyPointsOfInterest(query);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    private void postLure(@RequestParam double latitude, @RequestParam double longitude) {
        AddLureCommand command = new AddLureCommand(latitude, longitude);
        lureAdder.addLure(command);
    }
}
