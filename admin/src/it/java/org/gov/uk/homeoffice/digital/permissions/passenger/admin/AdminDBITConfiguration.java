package org.gov.uk.homeoffice.digital.permissions.passenger.admin;

import com.zaxxer.hikari.HikariDataSource;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.config.JDBIConfig;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu.WicuRepository;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
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
@Import(JDBIConfig.class)
public class AdminDBITConfiguration {

    @Autowired
    @Qualifier("accounts.db")
    private Jdbi dbi;

    @Bean
    @Qualifier("accounts.datasource")
    @ConfigurationProperties("test.datasource")
    public HikariDataSource accountsDataSource() {
        final HikariDataSource dataSource = DataSourceBuilder.create().type(HikariDataSource.class).build();
        dataSource.setPoolName("AccountsPool");
        return dataSource;
    }

    @Bean
    public WicuRepository wicuRepository() {
        return new WicuRepository(dbi);
    }

    @Bean
    SimpleMeterRegistry meterRegistry() {
        return new SimpleMeterRegistry();
    }
}
