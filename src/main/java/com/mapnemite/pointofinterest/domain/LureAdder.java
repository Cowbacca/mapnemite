package com.mapnemite.pointofinterest.domain;

import com.mapnemite.notification.domain.NotificationEventReceiver;
import com.mapnemite.notification.domain.event.SendNotificationCommand;
import com.mapnemite.pointofinterest.domain.event.AddLureCommand;
import com.mapnemite.pointofinterest.domain.event.LureDocument;
import com.mapnemite.pointofinterest.domain.location.Location;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.time.LocalDateTime;

@Component
public class LureAdder {

    private final LureRepository lureRepository;
    private final NotificationEventReceiver notificationEventReciever;

    @Inject
    public LureAdder(LureRepository lureRepository, NotificationEventReceiver notificationEventReciever) {
        this.lureRepository = lureRepository;
        this.notificationEventReciever = notificationEventReciever;
    }

    public LureDocument addLure(AddLureCommand addLureCommand) {
        Location location = new Location(addLureCommand.getLatitude(), addLureCommand.getLongitude());
        Lure lure = new Lure(location, LocalDateTime.now());
        lureRepository.save(lure);

        notificationEventReciever.send(new SendNotificationCommand());

        return new LureDocument(lure);
    }
}
