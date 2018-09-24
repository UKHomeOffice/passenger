package org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.db;

import org.jdbi.v3.core.argument.AbstractArgumentFactory;
import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.config.ConfigRegistry;

import java.sql.Types;
import java.util.Locale;

public class CountryArgumentFactory extends AbstractArgumentFactory<Locale> {

    public CountryArgumentFactory() {
        super(Types.CHAR);
    }

    @Override
    protected Argument build(Locale value, ConfigRegistry config) {
        return (position, statement, ctx) -> statement.setString(position, value.getISO3Country());
    }

}
