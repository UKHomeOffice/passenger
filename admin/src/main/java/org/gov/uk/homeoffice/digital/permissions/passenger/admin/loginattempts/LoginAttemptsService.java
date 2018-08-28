package org.gov.uk.homeoffice.digital.permissions.passenger.admin.loginattempts;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.LoginAttempt;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt.LoginAttemptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoginAttemptsService {

    @Autowired
    private LoginAttemptRepository loginAttemptRepository;

    public List<LoginAttempt> allLoginAttemptsBetween(LocalDateTime from, LocalDateTime to) {
        return loginAttemptRepository.allLoginAttemptsBetween(from, to);
    }

}

