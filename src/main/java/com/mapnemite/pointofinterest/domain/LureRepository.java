package com.mapnemite.pointofinterest.domain;

import com.mapnemite.pointofinterest.domain.location.Circle;

import java.util.Set;

public interface LureRepository {
    void save(Lure lure);

    Set<Lure> findByLocationWithinAndNotExpired(Circle circle);
}
