package org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.ui;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.service.CountryService;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Country;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CountryControllerTest {

    @Mock
    private CountryService mockCountryService;

    @InjectMocks
    private CountryController underTest;

    @Test
    public void shouldShowCountries() {
        final Model mockModel = Mockito.mock(Model.class);
        when(mockCountryService.getCountries()).thenReturn(List.of(new Country()));
        final String ui = underTest.show("all", mockModel);
        verify(mockModel).addAttribute(eq("form"), any(CountryListForm.class));
        Assert.assertThat(ui, is("country/country"));
    }

    @Test
    public void shouldUpdateCountries() {
        final Model mockModel = Mockito.mock(Model.class);
        final Country country = new Country(Locale.UK, Boolean.TRUE, "GBR", null, null);
        final Country dbCountry = new Country(Locale.UK, Boolean.FALSE, "GBR", null, null);
        final CountryListForm countryListForm = new CountryListForm();
        countryListForm.setCountries(List.of(country));
        when(mockCountryService.getCountryByCountryCode("GB")).thenReturn(Optional.of(dbCountry));
        underTest.update(countryListForm, mockModel);
        verify(mockCountryService).saveCountry(country);
    }

}