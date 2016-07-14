package com.mapnemite.domain;

import com.mapnemite.domain.event.AddLureCommand;
import com.mapnemite.domain.location.Location;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.time.LocalDateTime;

@Component
public class LureAdder {

    private final LureRepository lureRepository;

    @Inject
    public LureAdder(LureRepository lureRepository) {
        this.lureRepository = lureRepository;
    }

    public void addLure(AddLureCommand addLureCommand) {
        Location location = new Location(addLureCommand.getLatitude(), addLureCommand.getLongitude());
        Lure lure = new Lure(location, LocalDateTime.now());
        lureRepository.save(lure);
    }
}
