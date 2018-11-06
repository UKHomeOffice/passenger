package org.gov.uk.homeoffice.digital.permissions.passenger.admin.mi.service;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.mi.repository.MiRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.Month;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MiServiceBeanTest {

    @Mock
    private MiRepository miRepository;

    @InjectMocks
    private MiServiceBean underTest;

    @Test
    public void shouldGetVisaCountByStatusForMonth() {
        underTest.visaCountByStatusForMonth(Month.NOVEMBER, 2018);
        verify(miRepository).countVisaByStatus(
                LocalDateTime.of(2018, 11, 1, 0, 0, 0),
                LocalDateTime.of(2018, 11, 30, 23, 59, 59, 999999999));
    }

    @Test
    public void shouldGetLoginCountForMonth() {
        underTest.loginCountForMonth(Month.NOVEMBER, 2018);
        verify(miRepository).countSuccessfulLogin(
                LocalDateTime.of(2018, 11, 1, 0, 0, 0),
                LocalDateTime.of(2018, 11, 30, 23, 59, 59, 999999999));
    }

}