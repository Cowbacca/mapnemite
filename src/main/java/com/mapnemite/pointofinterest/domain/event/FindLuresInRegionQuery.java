package com.mapnemite.pointofinterest.domain.event;

import com.mapnemite.common.location.domain.Location;
import lombok.Value;

@Value
public class FindLuresInRegionQuery {
    private final Location northEast;
    private final Location southWest;
}
