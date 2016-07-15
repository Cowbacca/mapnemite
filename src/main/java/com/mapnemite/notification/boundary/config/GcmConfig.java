package com.mapnemite.notification.boundary.config;

import com.google.android.gcm.server.Sender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GcmConfig {
    @Bean
    public Sender sender(@Value("${gcm.key}") String key) {
        return new Sender(key);
    }
}
