package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

import org.gov.uk.homeoffice.digital.permissions.passenger.PassengerDBITConfiguration;
import org.gov.uk.homeoffice.digital.permissions.passenger.TruncateTablesBeforeEachTest;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaType;
import org.jdbi.v3.core.Jdbi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PassengerDBITConfiguration.class)
@TruncateTablesBeforeEachTest
public class VisaTypeDAOIT {

    @Autowired
    @Qualifier("passenger.db")
    private Jdbi dbi;

    @Test
    public void insertAndGetVisaType() {
        final VisaType visaType = VisaType.createVisaType("visa-type", "notes");
        dbi.useHandle(handle -> {
            final VisaTypeDAO dao = handle.attach(VisaTypeDAO.class);
            final Long id = dao.insert(visaType);
            assertTrue(dao.selectById(id).isPresent());
        });
    }

    @Test
    public void updateAndGetVisaType() {
        final VisaType visaType = VisaType.createVisaType("visa-type", "notes");
        dbi.useHandle(handle -> {
            final VisaTypeDAO dao = handle.attach(VisaTypeDAO.class);
            final Long id = dao.insert(visaType);
            final VisaType insertedVisaType = dao.selectById(id).orElseThrow();
            insertedVisaType.setEnabled(false);
            dao.update(visaType);
            final VisaType updatedVisaType = dao.selectById(id).orElseThrow();
            assertNotEquals(insertedVisaType, updatedVisaType);
        });
    }

    @Test
    public void deleteVisaType() {
        final VisaType visaType = VisaType.createVisaType("visa-type", "notes");
        dbi.useHandle(handle -> {
            final VisaTypeDAO dao = handle.attach(VisaTypeDAO.class);
            final Long id = dao.insert(visaType);
            dao.delete(id);
            assertFalse(dao.selectById(id).isPresent());
        });
    }

    @Test
    public void selectAll() {
        final VisaType visaType = VisaType.createVisaType("visa-type", "notes");
        dbi.useHandle(handle -> {
            final VisaTypeDAO dao = handle.attach(VisaTypeDAO.class);
            final Long id = dao.insert(visaType);
            Collection<VisaType> results = dao.selectAll();
            assertThat(results.size(), is(1));
        });
    }


}