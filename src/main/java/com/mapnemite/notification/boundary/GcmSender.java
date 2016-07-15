package com.mapnemite.notification.boundary;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Sender;
import com.mapnemite.notification.domain.NotificationSender;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.IOException;

@Component
public class GcmSender implements NotificationSender {
    private final Sender sender;

    @Inject
    public GcmSender(Sender sender) {
        this.sender = sender;
    }

    @Override
    public void send(String registrationId) {
        try {
            sender.sendNoRetry(new Message.Builder().build(), registrationId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
