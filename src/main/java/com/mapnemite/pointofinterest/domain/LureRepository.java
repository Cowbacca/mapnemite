package com.mapnemite.pointofinterest.domain;

import com.mapnemite.common.location.domain.Circle;
import com.mapnemite.common.location.domain.Rectangle;

import java.util.Set;

public interface LureRepository {
    void save(Lure lure);

    Set<Lure> findByLocationWithinAndNotExpired(Circle circle);

    Set<Lure> findByLocationWithinAndNotExpired(Rectangle rectangle);
}
