package com.processManager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@Import({ProcessManagerServiceConfig.class})
public class ProcessManagerApplication {

    @Value("${timezone}")
    private String TIMEZONE;

    @PostConstruct
    public void init() {
        if (TIMEZONE != null && !TIMEZONE.isEmpty())
            TimeZone.setDefault(TimeZone.getTimeZone(TIMEZONE));
    }

    public static void main(String[] args) {
        SpringApplication.run(ProcessManagerApplication.class, args);
    }
}
