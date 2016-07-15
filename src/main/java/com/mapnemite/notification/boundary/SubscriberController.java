package com.mapnemite.notification.boundary;

import com.mapnemite.notification.domain.NotificationSubscriber;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
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

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void putSubscriber(@RequestBody PushSubscription pushSubscription) {
        notificationSubscriber.subscribe(pushSubscription.subscribeCommand());
    }
}
