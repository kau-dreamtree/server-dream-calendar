package org.standard.dreamcalendar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
public class DreamCalendarApplication {

    public static void main(String[] args) {
        SpringApplication.run(DreamCalendarApplication.class, args);
    }

}
