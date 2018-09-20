package org.gov.uk.homeoffice.digital.permissions.passenger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;

@SpringBootApplication(exclude = { FlywayAutoConfiguration.class})
public class PassengerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PassengerApplication.class, args);  
    }

}  