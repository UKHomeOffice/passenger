package org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord;


import org.gov.uk.homeoffice.digital.permissions.passenger.domain.CrsRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Gender;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;
import static java.util.Optional.ofNullable;

public class CrsRecordMapper implements RowMapper<CrsRecord> {
    @Override
    public CrsRecord map(ResultSet resultSet, StatementContext ctx) throws SQLException {
        return CrsRecord.builder()
                .id(resultSet.getLong("id"))
                .gwfRef(resultSet.getString("gwf_ref"))
                .vafNo(resultSet.getString("vaf_no"))
                .casNo(resultSet.getString("cas_no"))
                .cosNo(resultSet.getString("cos_no"))
                .postName(resultSet.getString("post_name"))
                .familyName(resultSet.getString("family_name"))
                .otherName(resultSet.getString("other_name"))
                .gender(Gender.valueOf(resultSet.getString("gender")))
                .dateOfBirth(ofNullable(resultSet.getDate("date_of_birth")).map(val -> val.toLocalDate()).orElse(null))
                .nationality(resultSet.getString("nationality"))
                .passportNumber(resultSet.getString("passport_no"))
                .mobileNumber(resultSet.getString("mobile_no"))
                .emailAddress(resultSet.getString("email_address"))
                .localAddress(resultSet.getString("local_address"))
                .status(VisaStatus.parse(resultSet.getString("status")))
                .action(resultSet.getString("action"))
                .reason(resultSet.getString("reason"))
                .ecType(resultSet.getString("ec_type"))
                .entryType(resultSet.getString("entry_type"))
                .visaEndorsement(resultSet.getString("visa_endorsement"))
                .validFrom(ofNullable(resultSet.getDate("valid_from")).map(val -> val.toLocalDate()).orElse(null))
                .validTo(ofNullable(resultSet.getDate("valid_to")).map(val -> val.toLocalDate()).orElse(null))
                .workUntil(ofNullable(resultSet.getDate("work_until")).map(val -> val.toLocalDate()).orElse(null))
                .sponsorName(resultSet.getString("sponsor_name"))
                .sponsorType(resultSet.getString("sponsor_type"))
                .sponsorAddress(resultSet.getString("sponsor_address"))
                .sponsorSpxNo(resultSet.getString("sponsor_spx_no"))
                .additionalEndorsement1(resultSet.getString("add_endors_1"))
                .additionalEndorsement2(resultSet.getString("add_endors_2"))
                .catDEndors1(resultSet.getString("cat_d_endors_1"))
                .catDEndors2(resultSet.getString("cat_d_endors_2"))
                .uniCollegeName(resultSet.getString("uni_college_name"))
                .brpCollectionInfo(resultSet.getString("brp_collection_info"))
                .expectedTravelDate(ofNullable(resultSet.getDate("expected_travel_date")).map(val -> val.toLocalDate()).orElse(null))
                .updated(ofNullable(resultSet.getTimestamp("updated")).map(val -> val.toLocalDateTime()).orElse(null))
                .emailsSent(ofNullable(resultSet.getString("emails_sent")).map(val -> Arrays.asList(val.split(",")).stream().collect(Collectors.toSet())).orElse(emptySet()))
                .updatedBy(resultSet.getString("updated_by"))
                .build();

    }
}
