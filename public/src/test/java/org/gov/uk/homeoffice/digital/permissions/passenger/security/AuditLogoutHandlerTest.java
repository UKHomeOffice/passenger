package org.gov.uk.homeoffice.digital.permissions.passenger.security;

import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
import org.gov.uk.homeoffice.digital.permissions.passenger.authentication.RemoteIPThreadLocal;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.*;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleConstants;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visarecord.VisaRecordService;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class AuditLogoutHandlerTest {

    @Mock
    private AuditService auditService;
    @Mock
    private VisaRecordService visaRecordService;

    @InjectMocks
    private AuditLogoutHandler auditLogoutHandler;

    @Test
    @WithMockUser(username = "12345")
    public void logout() {
        when(visaRecordService.get("12345"))
                .thenReturn(new VisaRecord(VisaStatus.VALID,
                        VisaType.createVisaType("test", ""), visaRules()));

        RemoteIPThreadLocal.set("123.123.123.123");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        auditLogoutHandler.logout(null, null, authentication);

        verify(auditService).audit("action='logout', passportNumber='passportNumber123', IPAddress='123.123.123.123'", "SUCCESS", "PASSENGER");
    }

    private Collection<Tuple<VisaRule, Collection<VisaRuleContent>>> visaRules() {
        VisaRule passportNumberRule = new VisaRule(VisaRuleConstants.PASSPORT_NUMBER);
        Collection<VisaRuleContent> content = List.of(new VisaRuleContent(1L, VisaRuleConstants.PASSPORT_NUMBER,
                "passportNumber123", true, RuleType.USER_DATA));
        return List.of(Tuple.tpl(passportNumberRule, content));
    }

}