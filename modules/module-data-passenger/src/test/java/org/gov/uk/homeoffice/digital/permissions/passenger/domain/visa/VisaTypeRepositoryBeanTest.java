package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaType;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.action.FindAllVisaTypes;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.action.FindVisaTypeById;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.action.RemoveVisaTypeAction;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.action.SaveVisaTypeAction;
import org.jdbi.v3.core.Jdbi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class VisaTypeRepositoryBeanTest {

    private final VisaType visaType = VisaType.createVisaType("visa-type");

    @Mock
    private Jdbi mockJdbi;

    @InjectMocks
    private VisaTypeRepositoryBean visaTypeRepository;

    @Test
    public void shouldSaveVisaType() {
        visaTypeRepository.save(visaType);
        verify(mockJdbi).inTransaction(new SaveVisaTypeAction(visaType));
    }

    @Test
    public void shouldDeleteVisaType() {
        visaTypeRepository.remove(1L);
        verify(mockJdbi).useTransaction(new RemoveVisaTypeAction(1L));
    }

    @Test
    public void shouldFindVisaTypeById() {
        visaTypeRepository.findOneById(1L);
        verify(mockJdbi).inTransaction(new FindVisaTypeById(1L));
    }

    @Test
    public void shouldFindAllVisaTypes() {
        visaTypeRepository.findAll();
        verify(mockJdbi).withHandle(any(FindAllVisaTypes.class));
    }

}