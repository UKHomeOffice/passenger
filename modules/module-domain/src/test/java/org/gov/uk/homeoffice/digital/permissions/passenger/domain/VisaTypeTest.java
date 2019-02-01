package org.gov.uk.homeoffice.digital.permissions.passenger.domain;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

public class VisaTypeTest {

    @Test
    public void shouldCreateVisaTypeFromConstructor() {
        final VisaType visaType = new VisaType(1L, "visa-type", "notes", null, Boolean.TRUE, null);
        assertThat(visaType.getId(), is(1L));
        assertThat(visaType.getName(), is("visa-type"));
        assertThat(visaType.getNotes(), is("notes"));
        assertThat(visaType.getEnabled(), is(Boolean.TRUE));
        assertThat(visaType.getCreated(), is(nullValue()));
    }

    @Test
    public void shouldCreateNewVisaTypeFromFactory() {
        final VisaType visaType = VisaType.createVisaType("visa-type");
        assertThat(visaType.getId(), nullValue());
        assertThat(visaType.getName(), is("visa-type"));
        assertThat(visaType.getNotes(), nullValue());
        assertThat(visaType.getEnabled(), is(Boolean.TRUE));
        assertThat(visaType.getCreated(), is(nullValue()));
    }

    @Test
    public void shouldCreateNewVisaTypeFromFactoryWithNotes() {
        final VisaType visaType = VisaType.createVisaType("visa-type", "notes");
        assertThat(visaType.getId(), nullValue());
        assertThat(visaType.getName(), is("visa-type"));
        assertThat(visaType.getNotes(), is("notes"));
        assertThat(visaType.getEnabled(), is(Boolean.TRUE));
        assertThat(visaType.getCreated(), is(nullValue()));
    }

}