package com.mapnemite.domain;

import com.mapnemite.domain.location.Circle;

import java.util.Set;

public interface LureRepository {
    void save(Lure lure);

    Set<Lure> findByLocationWithinAndNotExpired(Circle circle);
}
