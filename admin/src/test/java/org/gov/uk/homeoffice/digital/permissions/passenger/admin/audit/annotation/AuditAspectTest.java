package org.gov.uk.homeoffice.digital.permissions.passenger.admin.audit.annotation;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.WithKeycloakUser;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.exceptions.NotFoundException;
import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Participant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class AuditAspectTest {

    @Autowired
    private AuditAspect auditAspect;

    @Autowired
    private AuditableController auditableController;

    @Autowired
    private HttpServletRequest request;

    @MockBean
    @Qualifier("audit.admin")
    private AuditService auditService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    @WithKeycloakUser
    public void onePathVariableParameter() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/participants/1234"))
                .andExpect(status().is(200));

        verify(auditService).audit(eq("action='view', entity='Participant', id='1234'"),
                eq("SUCCESS"), eq("test@test.com"));
    }

    @Test
    @WithKeycloakUser
    public void onePathVariableParameterAndOtherParameters() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/participants1/1234"))
                .andExpect(status().is(200));

        verify(auditService).audit(eq("action='view', entity='Participant', id='1234'"),
                eq("SUCCESS"), eq("test@test.com"));
    }

    @Test
    @WithKeycloakUser
    public void multiplePathVariableParametersAndOtherParameters() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/participants/9876/abcd"))
                .andExpect(status().is(200));

        verify(auditService).audit(eq("action='view', entity='Participant', anotherId='abcd', id='9876'"),
                eq("SUCCESS"), eq("test@test.com")
        );
    }

    @Test
    @WithKeycloakUser
    public void delete() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/deleteparticipants/1111"))
                .andExpect(status().is(302));

        verify(auditService).audit(eq("action='delete', entity='Participant', id='1111'"),
                eq("SUCCESS"), eq("test@test.com"));
    }

    @Test
    @WithKeycloakUser
    public void methodThrowingException() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get("/throwingexception/2222"))
                ;

        verify(auditService).audit(eq("action='view', entity='Participant', id='2222', " +
                        "error='org.gov.uk.homeoffice.digital.permissions.passenger.admin.exceptions.NotFoundException: non existing resource'"),
                eq("FAILURE"), eq("test@test.com"));
    }


    @Controller
    public static class AuditableController {

        @Audit(auditAction = AuditAction.VIEW, resourceClass = Participant.class)
        @RequestMapping(path = "/participants/{id}", method = GET)
        public String onePathVariableParameter(@PathVariable String id) {
            return "42";
        }

        @Audit(auditAction = AuditAction.VIEW, resourceClass = Participant.class)
        @RequestMapping(path = "/participants1/{id}", method = GET)
        public String onePathVariableParameterAndOtherParameters(ServletRequest req,
                                                                 @PathVariable String id,
                                                                 Authentication auth) {
            return "42";
        }

        @Audit(auditAction = AuditAction.VIEW, resourceClass = Participant.class)
        @RequestMapping(path = "/participants/{id}/{anotherId}", method = GET)
        public String multiplePathVariableParametersAndOtherParameters(ServletRequest req,
                                                                       @PathVariable String id,
                                                                       Authentication authentication,
                                                                       @PathVariable String anotherId,
                                                                       ServletResponse res) {
            return "42";
        }

        @Audit(auditAction = AuditAction.DELETE, resourceClass = Participant.class)
        @RequestMapping(path = "/deleteparticipants/{id}", method = POST)
        public String delete(@PathVariable String id) {
            return "redirect:/42";
        }

        @Audit(auditAction = AuditAction.VIEW, resourceClass = Participant.class)
        @RequestMapping(path = "/throwingexception/{id}", method = GET)
        public String methodThrowingException(@PathVariable String id) {
            throw new NotFoundException("non existing resource");
        }

        @ExceptionHandler(NotFoundException.class)
        public String simpleExceptionHandler() {
            return "databaseError";
        }
    }

    @Configuration
    @EnableWebMvc
    @EnableAspectJAutoProxy
    @Import({AuditAspect.class, AuditableController.class})
    public static class Config {

    }
}
