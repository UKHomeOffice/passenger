package org.gov.uk.homeoffice.digital.permissions.passenger.admin.retention.service;

import java.time.LocalDateTime;

public interface RetentionService {

    void removeOlderThan(LocalDateTime time);

}
