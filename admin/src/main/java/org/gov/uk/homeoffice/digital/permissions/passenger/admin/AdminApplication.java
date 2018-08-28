package org.gov.uk.homeoffice.digital.permissions.passenger.admin;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.config.JDBIConfig;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.config.KeycloakConfig;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.config.MvcConfig;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.config.PassengerJDBIConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ComponentScan( {
        "org.gov.uk.homeoffice.digital.permissions.passenger.admin",
        "org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa",
        "org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord",
        "org.gov.uk.homeoffice.digital.permissions.passenger.domain.visarecord",
        "org.keycloak.adapters.springsecurity"
})
@Import({PassengerJDBIConfig.class, JDBIConfig.class, MvcConfig.class, KeycloakConfig.class})
@EnableAutoConfiguration(exclude = { FlywayAutoConfiguration.class })
public class AdminApplication {

    public static void main(String[] args) {  
        SpringApplication.run(AdminApplication.class, args);
    }

    @Bean
    CommonAnnotationBeanPostProcessor commonAnnotationBeanPostProcessor() {
        return new CommonAnnotationBeanPostProcessor();
    }

}  