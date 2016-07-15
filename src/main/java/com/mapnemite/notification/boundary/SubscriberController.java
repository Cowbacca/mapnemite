package com.mapnemite.notification.boundary;

import com.mapnemite.notification.domain.NotificationSubscriber;
import com.mapnemite.notification.domain.event.SubscribeCommand;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.inject.Inject;

@RequestMapping("/subscribers")
@Controller
public class SubscriberController {
    private final NotificationSubscriber notificationSubscriber;

    @Inject
    public SubscriberController(NotificationSubscriber notificationSubscriber) {
        this.notificationSubscriber = notificationSubscriber;
    }

    @RequestMapping(value = "/{registrationId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void putSubscriber(@PathVariable String registrationId) {
        notificationSubscriber.subscribe(new SubscribeCommand(registrationId));
    }
}
