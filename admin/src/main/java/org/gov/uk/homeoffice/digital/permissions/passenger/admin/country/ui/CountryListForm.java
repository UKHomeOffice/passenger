package org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.ui;

import lombok.Data;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Country;

import java.util.List;

@Data
public class CountryListForm {

    private List<Country> countries;

}
