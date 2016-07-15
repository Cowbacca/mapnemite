package com.mapnemite.notification.boundary;

import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.mapnemite.notification.domain.Notification;
import com.mapnemite.notification.domain.NotificationSender;
import nl.martijndwars.webpush.GcmNotification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Utils;
import org.apache.http.client.fluent.Content;
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
import java.util.concurrent.Future;

@Component
public class GcmSender implements NotificationSender {
    private final PushService sender;

    @Inject
    public GcmSender(PushService sender) {
        Security.addProvider(new BouncyCastleProvider());
        this.sender = sender;
    }

    @Override
    public void send(Notification notification, String registrationId, String encodedUserPublicKey, String encodedUserAuth) {
        try {
            byte[] payload = payload(notification);
            PublicKey userPublicKey = Utils.loadPublicKey(encodedUserPublicKey);
            byte[] userAuth = BaseEncoding.base64Url().decode(encodedUserAuth);
            GcmNotification gcmNotification = new GcmNotification(registrationId, userPublicKey, userAuth, payload);

            Future<Content> httpResponse = sender.send(gcmNotification);
            System.out.println(httpResponse.get().asString());
        } catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchPaddingException | NoSuchProviderException | IllegalBlockSizeException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private byte[] payload(Notification notification) {
        Gson gson = new Gson();
        String json = gson.toJson(notification.asMap());
        return json.getBytes();
    }
}
