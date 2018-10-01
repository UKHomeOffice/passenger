package org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.db;

final class CountrySQL {

    static final String INSERT = "INSERT INTO countries (iso_country_code, export_country, enabled) VALUES (:country, :exportCountry, :enabled)";
    static final String UPDATE = "UPDATE countries SET enabled = :enabled, export_country = :exportCountry WHERE iso_country_code = :id";
    static final String SELECT_ALL = "SELECT * FROM countries ORDER BY iso_country_code ASC";
    static final String SELECT_BY_ID = "SELECT * FROM countries WHERE iso_country_code = :id";

}
