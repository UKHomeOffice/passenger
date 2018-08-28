package org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.CrsRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Gender;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus;
import org.jdbi.v3.core.statement.StatementContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;

import static java.sql.Date.valueOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CrsRecordMapperTest {

    public static final LocalDate VALID_FROM = LocalDate.parse("06/06/2018", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    public static final LocalDate VALID_TO = LocalDate.parse("05/07/2018", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    public static final LocalDate DATE_OF_BIRTH = LocalDate.parse("08/11/1980", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    public static final LocalDate EXPECTED_TRAVEL_DATE = LocalDate.parse("08/11/2016", DateTimeFormatter.ofPattern("dd/MM/yyyy"));

    @InjectMocks
    private CrsRecordMapper underTest;


    @Test
    public void testMapper() throws SQLException {
        final Timestamp ts = new Timestamp(Instant.now().getEpochSecond());
        final Date date = new Date(Instant.now().getEpochSecond());

        final ResultSet mockResultSet = mock(ResultSet.class);
        final StatementContext mockStatementContext = mock(StatementContext.class);

        when(mockResultSet.getLong("id")).thenReturn(900L);
        when(mockResultSet.getString("gwf_ref")).thenReturn("gwf_number");
        when(mockResultSet.getString("vaf_no")).thenReturn("vaf_number");
        when(mockResultSet.getString("cas_no")).thenReturn("cas_number");
        when(mockResultSet.getString("cos_no")).thenReturn("cos_number");
        when(mockResultSet.getString("post_name")).thenReturn("post_name");
        when(mockResultSet.getString("family_name")).thenReturn("family_name");
        when(mockResultSet.getString("other_name")).thenReturn("other_name");
        when(mockResultSet.getString("gender")).thenReturn("MALE");
        when(mockResultSet.getDate("date_of_birth")).thenReturn( valueOf(DATE_OF_BIRTH));
        when(mockResultSet.getString("nationality")).thenReturn("nationality");
        when(mockResultSet.getString("passport_no")).thenReturn("passport_number");
        when(mockResultSet.getString("mobile_no")).thenReturn("mobile_number");
        when(mockResultSet.getString("email_address")).thenReturn("email");
        when(mockResultSet.getString("local_address")).thenReturn("local_address");
        when(mockResultSet.getString("status")).thenReturn("status");
        when(mockResultSet.getString("ec_type")).thenReturn("ec_type");
        when(mockResultSet.getString("entry_type")).thenReturn("entry_type");
        when(mockResultSet.getString("visa_endorsement")).thenReturn("visa_endorsement");
        when(mockResultSet.getDate("valid_from")).thenReturn( valueOf(VALID_FROM));
        when(mockResultSet.getDate("valid_to")).thenReturn( valueOf(VALID_TO));
        when(mockResultSet.getString("sponsor_name")).thenReturn("sponsor_name");
        when(mockResultSet.getString("sponsor_type")).thenReturn("sponsor_type");
        when(mockResultSet.getString("sponsor_address")).thenReturn("sponsor_address");
        when(mockResultSet.getString("sponsor_spx_no")).thenReturn("sponsor_spx_no");
        when(mockResultSet.getString("add_endors_1")).thenReturn("add_endors_1");
        when(mockResultSet.getString("add_endors_2")).thenReturn("add_endors_2");
        when(mockResultSet.getString("cat_d_endors_1")).thenReturn("cat_d_endors_1");
        when(mockResultSet.getString("cat_d_endors_2")).thenReturn("cat_d_endors_2");
        when(mockResultSet.getString("uni_college_name")).thenReturn("uni_college_name");
        when(mockResultSet.getString("brp_collection_info")).thenReturn("brp_collection_info");
        when(mockResultSet.getDate("expected_travel_date")).thenReturn( valueOf(EXPECTED_TRAVEL_DATE));
        when(mockResultSet.getString("emails_sent")).thenReturn( "a@b.c");


        final CrsRecord result = underTest.map(mockResultSet, mockStatementContext);
        assertThat(result.getId(), is(900L));
        assertThat(result.getGwfRef(), is("gwf_number"));
        assertThat(result.getVafNo(), is("vaf_number"));
        assertThat(result.getCasNo(), is("cas_number"));
        assertThat(result.getCosNo(), is("cos_number"));
        assertThat(result.getPostName(), is("post_name"));
        assertThat(result.getFamilyName(), is("family_name"));
        assertThat(result.getOtherName(), is("other_name"));
        assertThat(result.getGender(), is(Gender.MALE));
        assertThat(result.getDateOfBirth(), is(DATE_OF_BIRTH));
        assertThat(result.getNationality(), is("nationality"));
        assertThat(result.getPassportNumber(), is("passport_number"));
        assertThat(result.getMobileNumber(), is("mobile_number"));
        assertThat(result.getNationality(), is("nationality"));
        assertThat(result.getPassportNumber(), is("passport_number"));
        assertThat(result.getMobileNumber(), is("mobile_number"));
        assertThat(result.getEmailAddress(), is("email"));
        assertThat(result.getLocalAddress(), is("local_address"));
        assertThat(result.getStatus(), is(VisaStatus.VALID));
        assertThat(result.getEcType(), is("ec_type"));
        assertThat(result.getEntryType(), is("entry_type"));
        assertThat(result.getVisaEndorsement(), is("visa_endorsement"));
        assertThat(result.getValidFrom(), is(VALID_FROM));
        assertThat(result.getValidTo(), is(VALID_TO));
        assertThat(result.getSponsorName(), is("sponsor_name"));
        assertThat(result.getSponsorType(), is("sponsor_type"));
        assertThat(result.getSponsorAddress(), is("sponsor_address"));
        assertThat(result.getSponsorSpxNo(), is("sponsor_spx_no"));
        assertThat(result.getAdditionalEndorsement1(), is("add_endors_1"));
        assertThat(result.getAdditionalEndorsement2(), is("add_endors_2"));
        assertThat(result.getSponsorSpxNo(), is("sponsor_spx_no"));
        assertThat(result.getCatDEndors1(), is("cat_d_endors_1"));
        assertThat(result.getCatDEndors2(), is("cat_d_endors_2"));
        assertThat(result.getUniCollegeName(), is("uni_college_name"));
        assertThat(result.getBrpCollectionInfo(), is("brp_collection_info"));
        assertThat(result.getExpectedTravelDate(), is(EXPECTED_TRAVEL_DATE));
        assertThat(result.getEmailsSent().toArray()[0], is("a@b.c"));
    }


}