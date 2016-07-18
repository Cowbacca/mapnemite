package com.mapnemite.pointofinterest.domain;

import com.mapnemite.pointofinterest.domain.location.Circle;
import com.mapnemite.pointofinterest.domain.location.Rectangle;

import java.util.Set;

public interface LureRepository {
    void save(Lure lure);

    Set<Lure> findByLocationWithinAndNotExpired(Circle rectangle);

    Set<Lure> findByLocationWithinAndNotExpired(Rectangle rectangle);
}
