package org.gov.uk.homeoffice.digital.permissions.passenger.admin.mi.service;

import lombok.EqualsAndHashCode;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.mi.repository.MiRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Collection;

@Service
@EqualsAndHashCode
public class MiServiceBean implements MiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MiServiceBean.class);

    private final MiRepository miRepository;

    @Autowired
    public MiServiceBean(final MiRepository miRepository) {
        this.miRepository = miRepository;
    }

    @Override
    public Collection<Tuple<VisaStatus, Integer>> visaCountByStatusForMonth(final Month month, int year) {
        LOGGER.debug("Getting MI visa count.");
        final Tuple<LocalDateTime, LocalDateTime> dateTimeTuple = generateDateRange(month, year);
        return miRepository.countVisaByStatus(dateTimeTuple.get_1(), dateTimeTuple.get_2());
    }

    @Override
    public Collection<Tuple<Boolean, Integer>> loginCountForMonth(final Month month, int year) {
        LOGGER.debug("Getting MI login count.");
        final Tuple<LocalDateTime, LocalDateTime> dateTimeTuple = generateDateRange(month, year);
        return miRepository.countSuccessfulLogin(dateTimeTuple.get_1(), dateTimeTuple.get_2());
    }

    private Tuple<LocalDateTime, LocalDateTime> generateDateRange(final Month month, int year) {
        final LocalDate fromDate = LocalDate.of(year, month, 1);
        final LocalDate toDate = LocalDate.of(year, month, fromDate.lengthOfMonth());
        return Tuple.tpl(
                LocalDateTime.of(fromDate, LocalTime.MIN),
                LocalDateTime.of(toDate, LocalTime.MAX)
        );
    }

}
