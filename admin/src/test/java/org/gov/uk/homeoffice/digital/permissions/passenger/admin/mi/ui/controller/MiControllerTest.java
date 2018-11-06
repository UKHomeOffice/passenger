package org.gov.uk.homeoffice.digital.permissions.passenger.admin.mi.ui.controller;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.mi.service.MiService;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.mi.ui.model.MiModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MiControllerTest {

    @Mock
    private MiService mockMiService;

    @InjectMocks
    private MiController underTest;

    @Test
    public void shouldGetPage() {
        final LocalDate now = LocalDate.now();

        ModelAndView result = underTest.GETmi();

        assertThat(result.getViewName(), is("mi/mi"));
        assertTrue(result.getModelMap().containsAttribute("miModel"));

        verify(mockMiService).visaCountByStatusForMonth(now.getMonth(), now.getYear());
        verify(mockMiService).loginCountForMonth(now.getMonth(), now.getYear());
    }

    @Test
    public void shouldPostPage() {
        final LocalDate now = LocalDate.now();

        MiModel model = new MiModel();
        model.setMonth(now.getMonth().getValue());
        model.setYear(now.getYear());

        ModelAndView result = underTest.POSTmi(model);

        assertThat(result.getViewName(), is("mi/mi"));
        assertTrue(result.getModelMap().containsAttribute("miModel"));

        verify(mockMiService).visaCountByStatusForMonth(now.getMonth(), now.getYear());
        verify(mockMiService).loginCountForMonth(now.getMonth(), now.getYear());
    }


}