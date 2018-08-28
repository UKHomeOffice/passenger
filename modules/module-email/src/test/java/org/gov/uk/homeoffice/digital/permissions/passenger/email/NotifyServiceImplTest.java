package org.gov.uk.homeoffice.digital.permissions.passenger.email;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.service.notify.Authentication;
import uk.gov.service.notify.NotificationClient;
import uk.gov.service.notify.NotificationClientException;
import uk.gov.service.notify.SendEmailResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NotifyServiceImplTest {

    private static final String EMAIL_ADDRESS = "chuck.norris@karate.com";
    private static final String BASE_URL = "http://base.url";
    private static final String SELF_URL = "http://self.url";

    @Mock
    private NotificationClient mockNotificationClient;

    @InjectMocks
    private NotifyServiceImpl underTest;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(underTest, "visaGrantedTemplateId", "visaGrantedTemplateId");
        ReflectionTestUtils.setField(underTest, "visaRevokedTemplateId", "visaRevokedTemplateId");
        ReflectionTestUtils.setField(underTest, "accountTemplateId", "accountTemplateId");
        ReflectionTestUtils.setField(underTest, "adminTextTemplateId", "adminTextTemplateId");
        ReflectionTestUtils.setField(underTest, "twoFactorTemplateId", "twoFactorTemplateId");
    }

    @Test
    public void shouldSendVisaGrantedEmail() throws NotificationClientException {
        final SendEmailResponse mockSendEmailResponse = mock(SendEmailResponse.class);
        final Map<String, String> personalisation = new HashMap<>();
        personalisation.put("FullName", "Chuck Norris");
        personalisation.put("VisaURL", BASE_URL + "/visa/status");

        when(mockNotificationClient.sendEmail("visaGrantedTemplateId", EMAIL_ADDRESS, personalisation, "yourReferenceString")).thenReturn(mockSendEmailResponse);

        underTest.sendVisaGrantedEmail(EMAIL_ADDRESS, visa(), participant(), BASE_URL);

        verify(mockNotificationClient).sendEmail("visaGrantedTemplateId", EMAIL_ADDRESS, personalisation, "yourReferenceString");
    }

    @Test
    public void shouldSendAccountEmail() throws NotificationClientException {
        final SendEmailResponse mockSendEmailResponse = mock(SendEmailResponse.class);
        final String uuid = UUID.randomUUID().toString();
        final Map<String, String> personalisation = new HashMap<>();
        personalisation.put("url", SELF_URL + "/accounts/activate/" + uuid);

        when(mockNotificationClient.sendEmail("accountTemplateId", EMAIL_ADDRESS, personalisation, "yourReferenceString")).thenReturn(mockSendEmailResponse);

        underTest.sendAccountEmail(EMAIL_ADDRESS, uuid, SELF_URL);

        verify(mockNotificationClient).sendEmail("accountTemplateId", EMAIL_ADDRESS, personalisation, "yourReferenceString");
    }

    @Test
    public void shouldSendVisaRevokedEmail() throws NotificationClientException {
        final SendEmailResponse mockSendEmailResponse = mock(SendEmailResponse.class);
        final Map<String, String> personalisation = new HashMap<>();
        personalisation.put("FullName", "Chuck Norris");
        personalisation.put("VisaURL", BASE_URL + "/visa/status");

        when(mockNotificationClient.sendEmail("visaRevokedTemplateId", EMAIL_ADDRESS, personalisation, "yourReferenceString")).thenReturn(mockSendEmailResponse);

        underTest.sendVisaRevokedEmail(EMAIL_ADDRESS, visa(), participant(), BASE_URL);

        verify(mockNotificationClient).sendEmail("visaRevokedTemplateId", EMAIL_ADDRESS, personalisation, "yourReferenceString");
    }

    @Test
    public void shouldSendAdminText() throws NotificationClientException {
        final Map<String, String> personalisation = new HashMap<>();
        personalisation.put("number", "number");
        underTest.sendAdminText("phone", "number");
        verify(mockNotificationClient).sendSms("adminTextTemplateId", "phone", personalisation, "reference");
    }

    @Test
    public void shouldSendTwoFactorSms() throws NotificationClientException {
        final Map<String, String> personalisation = new HashMap<>();
        personalisation.put("code", "code");
        underTest.sendTwoFactorSMS("phone", "code");
        verify(mockNotificationClient).sendSms("twoFactorTemplateId", "phone", personalisation, "reference");
    }

    private Participant participant() {
        return new ParticipantBuilder().withDefaults()
                .setFirstName("Chuck")
                .setSurName("Norris")
                .createParticipant();
    }

    private Visa visa() {
        return new VisaBuilder()
                .setId(1L)
                .setSpx("spx")
                .setReason("reason")
                .setStatus(VisaStatus.VALID)
                .createVisa();
    }

    @Test
    @Ignore
    public void testCreateBearerToken() {
        String issuer = "d5308254-ebc7-4e20-b3f1-79ddefea46da";
        String apiKey = "931856a4-818b-4ea1-92f2-b507a1e41256";
        String bearerToken = new Authentication().create(issuer, apiKey);
        System.out.println("Bearer " + bearerToken);
    }

}
