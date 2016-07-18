package com.mapnemite.pointofinterest.boundary;

import com.mapnemite.pointofinterest.domain.LureAdder;
import com.mapnemite.pointofinterest.domain.LureFinder;
import com.mapnemite.pointofinterest.domain.event.*;
import com.mapnemite.pointofinterest.domain.location.Location;
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

    @RequestMapping(method = RequestMethod.GET, params = {"latitude", "longitude", "radius"})
    @ResponseBody
    private NearbyLuresDocument getNearby(@RequestParam double latitude, @RequestParam double longitude, @RequestParam double radius) {
        FindNearbyPointsOfInterestQuery query = new FindNearbyPointsOfInterestQuery(latitude, longitude, radius);
        return lureFinder.findNearbyPointsOfInterest(query);
    }

    @RequestMapping(method = RequestMethod.GET, params = {"neLat", "neLong", "swLat", "swLong"})
    @ResponseBody
    private LuresInRegionDocument getInRegion(@RequestParam double neLat, @RequestParam double neLong, @RequestParam double swLat, @RequestParam double swLong) {
        FindLuresInRegionQuery query = new FindLuresInRegionQuery(new Location(neLat, neLong), new Location(swLat, swLong));
        return lureFinder.findLuresInRegion(query);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    private LureDocument postLure(@RequestParam double latitude, @RequestParam double longitude) {
        AddLureCommand command = new AddLureCommand(latitude, longitude);
        return lureAdder.addLure(command);
    }
}
