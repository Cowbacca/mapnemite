package com.mapnemite.pointofinterest.domain.event;

import com.mapnemite.common.location.domain.Location;
import lombok.Value;

import java.util.Set;

@Value
public class LuresInRegionDocument {
    private final Location northEast;
    private final Location southWest;
    private final Set<LureDocument> lures;
}
