package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visarecord;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class VisaRecordServiceBean implements VisaRecordService {

    private VisaRecordAdapter<String> visaRecordAdapter;

    @Autowired
    public VisaRecordServiceBean(@Value("${visa.datasource}") final String visaRecordAdapterName,
                                 final List<VisaRecordAdapter<String>> visaRecordAdapters) {
        this.visaRecordAdapter = visaRecordAdapters.stream()
                                    .filter(adapter -> adapter.getType().name().equals(visaRecordAdapterName))
                                    .findAny()
                                    .orElseThrow(UnknownAdapterException::new);
    }

    @Override
    public VisaRecord get(final String id) {
        return visaRecordAdapter.toVisaRecord(id);
    }

    @Override
    public Optional<String> getVisaIdentifier(String passportNumber, LocalDate dateOfBirth) {
        return visaRecordAdapter.getIdentifier(passportNumber, dateOfBirth);
    }

    @Override
    public Collection<VisaRecord> getValidWithinRange(LocalDate lowerValidDate, LocalDate upperValidDate) {
        return visaRecordAdapter.getValidWithinRange(lowerValidDate, upperValidDate);
    }

}
