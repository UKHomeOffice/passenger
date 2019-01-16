package org.gov.uk.homeoffice.digital.permissions.passenger.config;

import org.gov.uk.homeoffice.digital.permissions.passenger.security.VisaAuthenticationProvider;
import org.gov.uk.homeoffice.digital.permissions.passenger.security.web.authentication.PassportDOBAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
public class PublicWebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final VisaAuthenticationProvider authenticationProvider;
    private final LogoutHandler auditLogoutHandler;

    public PublicWebSecurityConfig(VisaAuthenticationProvider authenticationProvider, LogoutHandler auditLogoutHandler) {
        this.authenticationProvider = authenticationProvider;
        this.auditLogoutHandler = auditLogoutHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().disable().and()
                .csrf().and()
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/visa/2fa")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .addLogoutHandler(auditLogoutHandler)
                .permitAll();

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .maximumSessions(1);
    }

    private PassportDOBAuthenticationFilter authenticationFilter() throws Exception {
        PassportDOBAuthenticationFilter filter = new PassportDOBAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        //need to pass in the services as we create object using new rather than auto
        //configured
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/css/**/*", "/js/**/*", "/version", "/actuator/health", "/static/**/*", "/loginfailed",
                        "/govuk_template/**/*", "/footer/**");
    }

}