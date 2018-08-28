package org.gov.uk.homeoffice.digital.permissions.passenger.domain.entryclearance;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.EntryClearance;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.EntryClearanceBuilder;
import org.jdbi.v3.core.statement.StatementContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EntryClearanceMapperTest {

    private EntryClearance ec;
    private String passportNumber = "AB123456YZ";

    @InjectMocks
    private EntryClearanceMapper entryClearanceMapper;

    @Before
    public void setUp() throws Exception {
        ec = new EntryClearanceBuilder().setPassportNumber(passportNumber)
                .setStartDate(LocalDate.of(2017, Month.SEPTEMBER, 25))
                .setEndDate(LocalDate.of(2017, Month.OCTOBER, 24))
                .setVisaValidToDate(LocalDate.of(2017, Month.OCTOBER, 20))
                .setPassportNationality("USA")
                .setSurname("Collins")
                .setOtherNames("Alan Arthur")
                .setDateOfBirth(LocalDate.of(1997, Month.DECEMBER, 12))
                .setPassportNumber("JK567890")
                .createEntryClearance();
    }

    @Test
    public void testMapper() throws SQLException {
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.getString("passport_number")).thenReturn(passportNumber);
        when(mockResultSet.getDate("start_date")).thenReturn(new Date(ec.getStartDate().getYear(),ec.getStartDate().getMonth().getValue(),ec.getStartDate().getDayOfMonth()));
        when(mockResultSet.getDate("end_date")).thenReturn(new Date(ec.getEndDate().getYear(),ec.getEndDate().getMonth().getValue(),ec.getEndDate().getDayOfMonth()));
        when(mockResultSet.getString("passport_nationality")).thenReturn(ec.getPassportNationality());
        when(mockResultSet.getString("surname")).thenReturn(ec.getSurname());
        when(mockResultSet.getString("other_names")).thenReturn(ec.getOtherNames());
        when(mockResultSet.getDate("date_of_birth")).thenReturn(new Date(ec.getDateOfBirth().getYear(), ec.getDateOfBirth().getMonth().getValue(), ec.getDateOfBirth().getDayOfMonth()));
        when(mockResultSet.getString("vaf_number")).thenReturn(ec.getVafNumber());
        when(mockResultSet.getString("cas_number")).thenReturn(ec.getCasNumber());
        when(mockResultSet.getString("spx_number")).thenReturn(ec.getSpxNumber());
        when(mockResultSet.getDate("visa_valid_to_date")).thenReturn(new Date(ec.getVisaValidToDate().getYear(), ec.getVisaValidToDate().getMonth().getValue(), ec.getVisaValidToDate().getDayOfMonth()));
        when(mockResultSet.getString("conditions_1")).thenReturn(ec.getConditionsLine1());
        when(mockResultSet.getString("conditions_2")).thenReturn(ec.getConditionsLine2());

        entryClearanceMapper.map(mockResultSet, mock(StatementContext.class));
    }
}