package org.gov.uk.homeoffice.digital.permissions.passenger.admin.config;

import org.gov.uk.homeoffice.digital.permissions.passenger.email.NotifyService;
import org.gov.uk.homeoffice.digital.permissions.passenger.email.NotifyServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uk.gov.service.notify.NotificationClient;

import java.time.Clock;
import java.util.UUID;
import java.util.function.Supplier;

@Configuration
@ComponentScan({"org.gov.uk.homeoffice.digital.permissions.passenger.domain"})
public class MvcConfig implements WebMvcConfigurer {

    @Bean
    @Profile("default")
    public NotifyService emailService(@Value("${email.templates.applicant}") String participantEmailTemplateId,
                                      @Value("${email.templates.revoked}") String visaRevokedTemplateId,
                                      @Value("${email.templates.account}") String accountTemplateId,
                                      @Value("${phone.templates.admin}") String adminTextTemplateId,
                                      @Value("${sms.templates.twofactor}") String twoFactorTemplateId,
                                      NotificationClient notificationClient) {
        return new NotifyServiceImpl(participantEmailTemplateId, visaRevokedTemplateId, accountTemplateId, adminTextTemplateId, twoFactorTemplateId, notificationClient);
    }

    @Bean
    public NotificationClient notificationClient(@Value("${email.notification.key}") String apiKey) {
        return new NotificationClient(apiKey);
    }

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public Supplier<UUID> uuidSupplier() {
        return () -> UUID.randomUUID();
    }

}