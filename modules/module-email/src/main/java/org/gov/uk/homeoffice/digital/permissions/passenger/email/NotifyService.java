package org.gov.uk.homeoffice.digital.permissions.passenger.email;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Participant;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Visa;
import uk.gov.service.notify.SendEmailResponse;
import uk.gov.service.notify.SendSmsResponse;

import java.util.Optional;

public interface NotifyService {

    Optional<SendEmailResponse> sendVisaGrantedEmail(String emailAddress, Visa visa, Participant participant, String baseUrl);

    Optional<SendEmailResponse> sendVisaGrantedEmail(String emailAddress, String fullName, String surName, String baseUrl);

    Optional<SendEmailResponse>  sendAccountEmail(String email, String uuid, String selfUrl);

    Optional<SendEmailResponse> sendVisaRevokedEmail(String emailAddress, Visa visa, Participant participant, String baseUrl);

    Optional<SendEmailResponse> sendVisaRevokedEmail(String emailAddress, String fullName, String surName, String baseUrl);

    Optional<SendSmsResponse> sendAdminText(String phone, String number);

    Optional<SendSmsResponse> sendTwoFactorSMS(String phoneNumber, String code);

}
