package com.mapnemite.notification.boundary;

import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.mapnemite.notification.domain.Notification;
import com.mapnemite.notification.domain.NotificationSender;
import nl.martijndwars.webpush.GcmNotification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Utils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.inject.Inject;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.ExecutionException;

@Component
public class GcmSender implements NotificationSender {
    private final PushService sender;
    private final Gson gson;

    @Inject
    public GcmSender(PushService sender, Gson gson) {
        this.gson = gson;
        this.sender = sender;

        Security.addProvider(new BouncyCastleProvider());
    }

    @Override
    public void send(Notification notification, String registrationId, String encodedUserPublicKey, String encodedUserAuth) {
        try {
            byte[] payload = payload(notification);
            PublicKey userPublicKey = Utils.loadPublicKey(encodedUserPublicKey);
            byte[] userAuth = BaseEncoding.base64Url().decode(encodedUserAuth);
            GcmNotification gcmNotification = new GcmNotification(registrationId, userPublicKey, userAuth, payload);

            sender.send(gcmNotification);
        } catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchPaddingException | NoSuchProviderException | IllegalBlockSizeException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private byte[] payload(Notification notification) {
        String json = gson.toJson(notification.asMap());
        return json.getBytes();
    }
}
