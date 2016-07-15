package com.mapnemite.pointofinterest.domain;

import com.mapnemite.notification.domain.NotificationToSubscribersPusher;
import com.mapnemite.pointofinterest.domain.event.AddLureCommand;
import com.mapnemite.pointofinterest.domain.event.LureDocument;
import com.mapnemite.pointofinterest.domain.location.Location;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.time.LocalDateTime;

@Component
public class LureAdder {

    private final LureRepository lureRepository;
    private final NotificationToSubscribersPusher notificationEventReciever;

    @Inject
    public LureAdder(LureRepository lureRepository, NotificationToSubscribersPusher notificationEventReciever) {
        this.lureRepository = lureRepository;
        this.notificationEventReciever = notificationEventReciever;
    }

    public LureDocument addLure(AddLureCommand addLureCommand) {
        Location location = new Location(addLureCommand.getLatitude(), addLureCommand.getLongitude());
        Lure lure = new Lure(location, LocalDateTime.now());
        lureRepository.save(lure);

        notificationEventReciever.pushNotificationToSubscribers(new LurePlacedNotification(lure));

        return new LureDocument(lure);
    }
}
