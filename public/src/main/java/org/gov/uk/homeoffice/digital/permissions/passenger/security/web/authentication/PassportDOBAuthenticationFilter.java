package org.gov.uk.homeoffice.digital.permissions.passenger.security.web.authentication;

import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;

public class PassportDOBAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final String PASSPORT_NUMBER_PARAMETER = "passportNumber";
    private static final String DOB_DAY_PARAMETER = "dob-day";
    private static final String DOB_MONTH_PARAMETER = "dob-month";
    private static final String DOB_YEAR_PARAMETER = "dob-year";

    public PassportDOBAuthenticationFilter() {
        setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/loginfailed"));
    }

    protected String obtainPassword(HttpServletRequest request) {
        String dobDay = obtain(DOB_DAY_PARAMETER, request);
        String dobMonth = obtain(DOB_MONTH_PARAMETER, request);
        String dobYear = obtain(DOB_YEAR_PARAMETER, request);

        return String.format("%s %s %s", dobDay, dobMonth, dobYear);
    }

    protected String obtainUsername(HttpServletRequest request) {
        return obtain(PASSPORT_NUMBER_PARAMETER, request);
    }

    private String obtain(String field, HttpServletRequest request) {
        return request.getParameter(field).trim();
    }

}
