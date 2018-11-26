package org.gov.uk.homeoffice.digital.permissions.passenger.admin.retention.service;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.CrsRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CRSRetentionServiceBean implements RetentionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CRSRetentionServiceBean.class);

    private final CrsRecordRepository crsRecordRepository;

    @Autowired
    public CRSRetentionServiceBean(final CrsRecordRepository crsRecordRepository) {
        this.crsRecordRepository = crsRecordRepository;
    }

    @Override
    public void removeOlderThan(final LocalDateTime dateTime) {
        LOGGER.info("Removing CRS data older than {}", dateTime.toString());
        crsRecordRepository.deleteOlderThan(dateTime);
    }

}
