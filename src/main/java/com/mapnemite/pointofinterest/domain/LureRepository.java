package com.mapnemite.pointofinterest.domain;

import com.mapnemite.common.location.domain.location.Circle;
import com.mapnemite.common.location.domain.location.Rectangle;

import java.util.Set;

public interface LureRepository {
    void save(Lure lure);

    Set<Lure> findByLocationWithinAndNotExpired(Circle circle);

    Set<Lure> findByLocationWithinAndNotExpired(Rectangle rectangle);
}
