package com.mapnemite.pointofinterest.domain.event;

import com.mapnemite.pointofinterest.domain.location.Location;
import lombok.Value;

import java.util.Set;

@Value
public class LuresInRegionDocument {
    private final Location northEast;
    private final Location southWest;
    private final Set<LureDocument> lures;
}
