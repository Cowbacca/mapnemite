package com.mapnemite.pointofinterest.domain.event;

import com.mapnemite.pointofinterest.domain.location.Location;
import lombok.Value;

@Value
public class FindLuresInRegionQuery {
    private final Location northEast;
    private final Location southWest;
}
