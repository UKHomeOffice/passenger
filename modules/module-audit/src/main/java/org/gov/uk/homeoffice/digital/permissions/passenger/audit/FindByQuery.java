package org.gov.uk.homeoffice.digital.permissions.passenger.audit;

import com.google.common.base.Strings;
import org.gov.uk.homeoffice.digital.permissions.passenger.audit.domain.Audit;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.JdbiException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Objects;

import static java.time.format.DateTimeFormatter.ISO_DATE;

public class FindByQuery implements HandleCallback<Collection<Audit>, JdbiException> {

    private String adminEmailAddress;
    private String passengerEmailAddress;
    private String passengerPassportNumber;
    private String passengerName;
    private LocalDate from;
    private LocalDate to;

    public FindByQuery(final String adminEmailAddress,
                       final String passengerEmailAddress,
                       final String passengerPassportNumber,
                       final String passengerName,
                       final LocalDate from,
                       final LocalDate to) {
        this.adminEmailAddress = adminEmailAddress;
        this.passengerEmailAddress = passengerEmailAddress;
        this.passengerPassportNumber = passengerPassportNumber;
        this.passengerName = Strings.isNullOrEmpty(passengerName) ? null : "%" + passengerName + "%";
        this.from = from == null ? LocalDate.of(2000, 01, 01) : from;
        this.to = to == null ? LocalDate.now().plusDays(1) : to;
    }

    @Override
    public Collection<Audit> withHandle(final Handle handle) throws JdbiException {
        return handle.attach(AuditDAO.class).selectByQuery(adminEmailAddress, passengerPassportNumber,
                passengerName, passengerEmailAddress, from.format(ISO_DATE), to.format(ISO_DATE));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FindByQuery that = (FindByQuery) o;
        return Objects.equals(adminEmailAddress, that.adminEmailAddress) &&
                Objects.equals(passengerEmailAddress, that.passengerEmailAddress) &&
                Objects.equals(passengerPassportNumber, that.passengerPassportNumber) &&
                Objects.equals(passengerName, that.passengerName) &&
                Objects.equals(from, that.from) &&
                Objects.equals(to, that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adminEmailAddress, passengerEmailAddress, passengerPassportNumber, passengerName, from, to);
    }
}
