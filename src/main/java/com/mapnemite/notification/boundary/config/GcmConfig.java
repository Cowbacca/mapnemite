package com.mapnemite.notification.boundary.config;

import com.google.gson.Gson;
import nl.martijndwars.webpush.PushService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GcmConfig {
    @Bean
    public PushService sender(@Value("${gcm.key}") String key) {
        return new PushService(key);
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }
}
