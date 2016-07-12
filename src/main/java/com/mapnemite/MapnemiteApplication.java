package com.mapnemite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
public class MapnemiteApplication {
    public static void main(String... args) {
        SpringApplication.run(MapnemiteApplication.class);
    }

    @RequestMapping("/")
    public String index() {
        return "index";
    }
}
