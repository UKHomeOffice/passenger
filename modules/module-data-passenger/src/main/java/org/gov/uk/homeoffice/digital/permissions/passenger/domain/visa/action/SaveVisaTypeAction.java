package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaType;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaTypeDAO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.JdbiException;

import java.util.Objects;
import java.util.Optional;

public class SaveVisaTypeAction implements HandleCallback<VisaType, JdbiException> {

    private VisaType visaType;

    public SaveVisaTypeAction(final VisaType visaType) {
        this.visaType = visaType;
    }

    @Override
    public VisaType withHandle(final Handle handle) throws JdbiException {
        final VisaTypeDAO dao = handle.attach(VisaTypeDAO.class);
        final Long id = Optional.ofNullable(visaType.getId()).map(visaType -> update(dao)).orElse(insert(dao));
        return dao.selectById(id).orElseThrow();
    }

    private Long update(final VisaTypeDAO dao) {
        dao.update(visaType);
        return visaType.getId();
    }

    private Long insert(final VisaTypeDAO dao) {
        return dao.insert(visaType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaveVisaTypeAction that = (SaveVisaTypeAction) o;
        return Objects.equals(visaType, that.visaType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(visaType);
    }

}
