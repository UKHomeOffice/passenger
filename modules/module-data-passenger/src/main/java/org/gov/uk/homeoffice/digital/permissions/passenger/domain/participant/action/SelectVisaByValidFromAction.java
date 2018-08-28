package org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Participant;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant.ParticipantDAO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.JdbiException;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class SelectVisaByValidFromAction implements HandleCallback<List<Participant>, JdbiException> {

    private final LocalDate lowerLimitIncluded;
    private final LocalDate upperLimitIncluded;

    public SelectVisaByValidFromAction(LocalDate lowerLimitIncluded, LocalDate upperLimitIncluded) {
        this.lowerLimitIncluded = lowerLimitIncluded;
        this.upperLimitIncluded = upperLimitIncluded;
    }

    @Override
    public List<Participant> withHandle(Handle handle) throws JdbiException {
        return handle.attach(ParticipantDAO.class).getByVisaValidFrom(lowerLimitIncluded, upperLimitIncluded);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SelectVisaByValidFromAction that = (SelectVisaByValidFromAction) o;
        return Objects.equals(lowerLimitIncluded, that.lowerLimitIncluded) &&
                Objects.equals(upperLimitIncluded, that.upperLimitIncluded);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lowerLimitIncluded, upperLimitIncluded);
    }
}
