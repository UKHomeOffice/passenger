package org.gov.uk.homeoffice.digital.permissions.passenger.config;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.gov.uk.homeoffice.digital.permissions.passenger.authentication.RemoteIPThreadLocal;
import org.gov.uk.homeoffice.digital.permissions.passenger.email.NotifyService;
import org.gov.uk.homeoffice.digital.permissions.passenger.email.NotifyServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring5.SpringTemplateEngine;
import uk.gov.service.notify.NotificationClient;

import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Configuration
@Order(4)
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener(){
            @Override
            public void requestInitialized(ServletRequestEvent requestEvent) {
                final HttpServletRequest request = (HttpServletRequest) requestEvent.getServletRequest();
                final String remote = Optional.ofNullable(request.getHeader("x-forwarded-for")).map(val -> val.split(",")[0]).orElseGet(() -> request.getRemoteAddr());
                RemoteIPThreadLocal.set(remote);
                super.requestInitialized(requestEvent);
            }

            @Override
            public void requestDestroyed(ServletRequestEvent requestEvent) {
                RemoteIPThreadLocal.unset();
                super.requestDestroyed(requestEvent);
            }
        };
    }

    @Bean
    public NotificationClient notificationClient(@Value("${email.notification.key}") String apiKey) {
        return new NotificationClient(apiKey);
    }

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
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }

}