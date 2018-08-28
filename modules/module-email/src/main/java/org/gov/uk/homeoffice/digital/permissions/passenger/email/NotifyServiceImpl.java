package org.gov.uk.homeoffice.digital.permissions.passenger.email;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Participant;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Visa;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.service.notify.NotificationClient;
import uk.gov.service.notify.NotificationClientException;
import uk.gov.service.notify.SendEmailResponse;
import uk.gov.service.notify.SendSmsResponse;

import java.util.Map;
import java.util.Optional;

import static org.gov.uk.homeoffice.digital.permissions.passenger.utils.CollectionUtils.map;
import static org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple.tpl;

public class NotifyServiceImpl implements NotifyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotifyServiceImpl.class);

    private final NotificationClient notificationClient;
    private final String visaGrantedTemplateId;
    private final String visaRevokedTemplateId;
    private final String accountTemplateId;
    private final String adminTextTemplateId;
    private final String twoFactorTemplateId;

    public NotifyServiceImpl(String visaGrantedTemplateId,
                             String visaRevokedTemplateId,
                             String accountTemplateId,
                             String adminTextTemplateId,
                             String twoFactorTemplateId,
                             NotificationClient notificationClient) {
        this.visaRevokedTemplateId = visaRevokedTemplateId;
        this.accountTemplateId = accountTemplateId;
        this.adminTextTemplateId = adminTextTemplateId;
        this.notificationClient = notificationClient;
        this.twoFactorTemplateId = twoFactorTemplateId;
        this.visaGrantedTemplateId = visaGrantedTemplateId;
    }

    @Override
    public Optional<SendEmailResponse> sendVisaGrantedEmail(String emailAddress, Visa visa, Participant participant, String baseUrl) {
        return sendMail(emailAddress, map(tpl("FullName", participant.getFirstName() + " " + participant.getSurName()), tpl("VisaURL", baseUrl + "/visa/status")), this.visaGrantedTemplateId);
    }

    @Override
    public Optional<SendEmailResponse> sendVisaGrantedEmail(String emailAddress, String fullName, String surName, String baseUrl) {
        return sendMail(emailAddress, map(tpl("FullName", fullName + " " + surName), tpl("VisaURL", baseUrl + "/visa/status")), this.visaGrantedTemplateId);
    }


    @Override
    public Optional<SendEmailResponse> sendAccountEmail(String email, String uuid,  String selfUrl) {
        try {
            SendEmailResponse response = notificationClient.sendEmail(accountTemplateId, email, map(tpl("url", selfUrl + "/accounts/activate/" + uuid)), "yourReferenceString");
            LOGGER.info("email sending successful", response);
            return Optional.of(response);
        } catch (Exception e) {
            LOGGER.warn("email sending failed", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<SendEmailResponse> sendVisaRevokedEmail(String emailAddress, Visa visa, Participant participant, String baseUrl) {
        return sendMail(emailAddress, map(tpl("FullName", participant.getFirstName() + " " + participant.getSurName()), tpl("VisaURL", baseUrl + "/visa/status")), this.visaRevokedTemplateId);
    }

    @Override
    public Optional<SendEmailResponse> sendVisaRevokedEmail(String emailAddress, String fullName, String surName, String baseUrl) {
        return sendMail(emailAddress, map(tpl("FullName", fullName + " " + surName), tpl("VisaURL", baseUrl + "/visa/status")), this.visaRevokedTemplateId);
    }

    @Override
    public Optional<SendSmsResponse> sendAdminText(String phone, String number) {
        try {
            final SendSmsResponse sendSmsResponse = notificationClient.sendSms(adminTextTemplateId, phone, map(tpl("number", number)), "reference");
            return Optional.ofNullable(sendSmsResponse);
        } catch (NotificationClientException e) {
           return Optional.empty();
        }
    }

    @Override
    public Optional<SendSmsResponse> sendTwoFactorSMS(String phoneNumber, String code) {
        try {
            final SendSmsResponse sendSmsResponse = notificationClient.sendSms(twoFactorTemplateId, phoneNumber,
                    map(tpl("code", code)), "reference");
            return Optional.ofNullable(sendSmsResponse);
        } catch (NotificationClientException e) {
            return Optional.empty();
        }
    }

    private Optional<SendEmailResponse> sendMail(String emailAddress, Map<String, String> personalisation, String templateId) {
        try {
            SendEmailResponse response = notificationClient.sendEmail(templateId, emailAddress, personalisation, "yourReferenceString");
            LOGGER.info("email sending to " + emailAddress + " successful", response);
            return Optional.of(response);
        } catch (Exception e) {
            LOGGER.warn("email sending to " + emailAddress + " failed", e);
            return Optional.empty();
        }
    }

}