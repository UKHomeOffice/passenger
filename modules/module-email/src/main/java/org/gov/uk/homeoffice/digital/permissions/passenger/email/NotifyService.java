package org.gov.uk.homeoffice.digital.permissions.passenger.email;

import uk.gov.service.notify.SendEmailResponse;
import uk.gov.service.notify.SendSmsResponse;

import java.util.Optional;

public interface NotifyService {

    Optional<SendEmailResponse> sendVisaGrantedEmail(String emailAddress, String fullName, String surName, String baseUrl);

    Optional<SendEmailResponse>  sendAccountEmail(String email, String uuid, String selfUrl);

    Optional<SendEmailResponse> sendVisaRevokedEmail(String emailAddress, String fullName, String surName, String baseUrl);

    Optional<SendEmailResponse> sendTechnicalIssueEmail(String emailAddress, String name, String passportNumber, String issueDetail);

    Optional<SendSmsResponse> sendAdminText(String phone, String number);

    Optional<SendSmsResponse> sendTwoFactorSMS(String phoneNumber, String code);

}
