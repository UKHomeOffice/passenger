package org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.service;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.db.CountryRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class CountryServiceBean implements CountryService {

    private final CountryRepository countryRepository;
    private final AuditService auditService;

    @Autowired
    public CountryServiceBean(final CountryRepository countryRepository,
                              @Qualifier("audit.admin") final AuditService auditService) {
        this.countryRepository = countryRepository;
        this.auditService = auditService;
    }

    @Override
    public Collection<Country> getCountries() {
        return countryRepository.findAll();
    }

    @Override
    public Optional<Country> getCountryByCountryCode(final String countryCode) {
        return countryRepository.findById(countryCode);
    }

    @Override
    public void saveCountry(final Country country) {
        auditService.audit(String.format("action='Update Country (%s), enabled=%s'", country.getExportCountry(),
                country.getEnabled().toString()),"SUCCESS");
        countryRepository.save(country);
    }

}
