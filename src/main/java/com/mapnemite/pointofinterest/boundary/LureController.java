package com.mapnemite.pointofinterest.boundary;

import com.mapnemite.pointofinterest.domain.LureAdder;
import com.mapnemite.pointofinterest.domain.LureFinder;
import com.mapnemite.pointofinterest.domain.event.AddLureCommand;
import com.mapnemite.pointofinterest.domain.event.FindNearbyPointsOfInterestQuery;
import com.mapnemite.pointofinterest.domain.event.LureDocument;
import com.mapnemite.pointofinterest.domain.event.NearbyLuresDocument;
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
    @ResponseBody
    private LureDocument postLure(@RequestParam double latitude, @RequestParam double longitude) {
        AddLureCommand command = new AddLureCommand(latitude, longitude);
        return lureAdder.addLure(command);
    }
}
