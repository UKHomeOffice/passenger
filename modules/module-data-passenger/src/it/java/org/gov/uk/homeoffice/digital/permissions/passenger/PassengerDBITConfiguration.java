package org.gov.uk.homeoffice.digital.permissions.passenger;

import com.zaxxer.hikari.HikariDataSource;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.config.PassengerJDBIConfig;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableConfigurationProperties
@PropertySource("classpath:test.properties")
@Import(PassengerJDBIConfig.class)
public class PassengerDBITConfiguration {

    @Autowired
    @Qualifier("passenger.db")
    private Jdbi dbi;

    @Bean
    @Qualifier("passenger.datasource")
    @ConfigurationProperties("test.datasource")
    public HikariDataSource passengerDataSource() {
        final HikariDataSource dataSource = DataSourceBuilder.create().type(HikariDataSource.class).build();
        dataSource.setPoolName("PassengerPool");
        return dataSource;
    }

    @Bean
    public AuditService auditService(@Qualifier("passenger.db") final Jdbi jdbi, final MeterRegistry meterRegistry) {
        return new AuditService(jdbi, meterRegistry);
    }

    @Bean
    SimpleMeterRegistry meterRegistry() {
        return new SimpleMeterRegistry();
    }
}
