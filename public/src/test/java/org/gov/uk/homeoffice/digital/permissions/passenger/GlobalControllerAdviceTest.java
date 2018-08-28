package org.gov.uk.homeoffice.digital.permissions.passenger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource(properties = {
        "passenger.tracking.google.token=" + GlobalControllerAdviceTest.EXPECTED_GOOGLE_TOKEN,
})
public class GlobalControllerAdviceTest {

    public static final String EXPECTED_GOOGLE_TOKEN = "google_token";

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
        Model model = mock(Model.class);

        Authentication authentication = when(mock(Authentication.class).isAuthenticated()).thenReturn(true).getMock();

        globalControllerAdvice.globalAttributes(model, authentication);

        verify(model).addAttribute("authenticated", true);
        verify(model).addAttribute("googleToken", EXPECTED_GOOGLE_TOKEN);
    }

    @Test
    public void modelPropertiesAreSetWhenNotAuthenticated() {
        Model model = mock(Model.class);

        Authentication authentication = when(mock(Authentication.class).isAuthenticated()).thenReturn(false).getMock();

        globalControllerAdvice.globalAttributes(model, authentication);

        verify(model).addAttribute("authenticated", false);
        verify(model).addAttribute("googleToken", EXPECTED_GOOGLE_TOKEN);
    }

    @Test
    public void modelPropertiesAreSetWhithNoAuthentication() {
        Model model = mock(Model.class);

        Authentication authentication = null;

        globalControllerAdvice.globalAttributes(model, authentication);

        verify(model).addAttribute("authenticated", false);
        verify(model).addAttribute("googleToken", EXPECTED_GOOGLE_TOKEN);
    }

    @Test
    public void theAdviceIsExecutedInControllers() throws Exception {
        mockMvc.perform(get("/foo"))
                .andExpect(MockMvcResultMatchers.model().attribute("set_by_controller",42))
                .andExpect(MockMvcResultMatchers.model().attribute("authenticated",false))
                .andExpect(MockMvcResultMatchers.model().attribute("googleToken",EXPECTED_GOOGLE_TOKEN));
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
