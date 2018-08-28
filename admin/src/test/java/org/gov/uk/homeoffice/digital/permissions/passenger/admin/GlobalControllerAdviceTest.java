package org.gov.uk.homeoffice.digital.permissions.passenger.admin;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GlobalControllerAdviceTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private GlobalControllerAdvice globalControllerAdvice;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void modelPropertiesAreSetWhenAuthenticated() {
        Map<String, Object> model = new HashMap<>();
        Authentication authentication = when(mock(Authentication.class).isAuthenticated()).thenReturn(true).getMock();
        HttpServletResponse response = when(mock(HttpServletResponse.class).getStatus()).thenReturn(200).getMock();

        globalControllerAdvice.globalAttributes(model, authentication, response);

        assertThat(model, is(
                ImmutableMap.of(
                        "authenticated", true,
                        "status", 200
                )
        ));
    }

    @Test
    public void modelPropertiesAreSetWhenNotAuthenticated() {
        Map<String, Object> model = new HashMap<>();
        Authentication authentication = when(mock(Authentication.class).isAuthenticated()).thenReturn(false).getMock();
        HttpServletResponse response = when(mock(HttpServletResponse.class).getStatus()).thenReturn(401).getMock();

        globalControllerAdvice.globalAttributes(model, authentication, response);

        assertThat(model, is(
                ImmutableMap.of(
                        "authenticated", false,
                        "status", 401
                )
        ));
    }

    @Test
    public void modelPropertiesAreSetWhithNoAuthentication() {
        Map<String, Object> model = new HashMap<>();
        Authentication authentication = null;
        HttpServletResponse response = when(mock(HttpServletResponse.class).getStatus()).thenReturn(401).getMock();

        globalControllerAdvice.globalAttributes(model, authentication, response);

        assertThat(model, is(
                ImmutableMap.of(
                        "authenticated", false,
                        "status", 401
                )
        ));
    }

    @Test
    public void theAdviceIsExecutedInControllers() throws Exception {
        int expectedStatus = 200;
        mockMvc.perform(get("/foo"))
                .andExpect(status().is(expectedStatus))
                .andExpect(model().attribute("set_by_controller", 42))
                .andExpect(model().attribute("authenticated", false))
                .andExpect(model().attribute("status", expectedStatus));
    }

    @Controller
    public static class SimpleController {
        @RequestMapping("/foo")
        public ModelAndView foo(Model model) {
            model.addAttribute("set_by_controller", 42);
            return new ModelAndView("bar", model.asMap());
        }
    }

    @Configuration
    public static class Config {
        @Bean
        public GlobalControllerAdvice globalControllerAdvice() {
            return new GlobalControllerAdvice();
        }

        @Bean
        public SimpleController simpleController() {
            return new SimpleController();
        }
    }
}
