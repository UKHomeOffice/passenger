package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Visa;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaBuilder;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.action.SaveVisaAction;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.action.SelectByPassportNumberAction;
import org.jdbi.v3.core.Jdbi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class VisaRepositoryTest {

    @Mock
    private Jdbi mockJdbi;

    @InjectMocks
    private VisaRepositoryBean visaRepository;

    @Test
    public void saveVisa() {
        final String passportNumber = "ABC";
        Visa visa = new VisaBuilder()
                .setPassportNumber(passportNumber)
                .setCatDEndorsements(asList("T4 G Student SPX Work limit 10 hrs,p/w term time.", "No Public Funds"))
                .createVisa();

        visaRepository.save(visa);
        verify(mockJdbi).useTransaction(new SaveVisaAction(visa));
    }

    @Test
    public void getVisaByPassportNumber() {
        final String passportNumber = "abc";
        visaRepository.getByPassportNumber(passportNumber);
        verify(mockJdbi).withHandle(new SelectByPassportNumberAction(passportNumber));
    }

}
