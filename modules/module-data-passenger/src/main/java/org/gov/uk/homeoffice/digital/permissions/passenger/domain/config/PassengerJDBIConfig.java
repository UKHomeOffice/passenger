package org.gov.uk.homeoffice.digital.permissions.passenger.domain.config;

import com.zaxxer.hikari.HikariDataSource;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.flywaydb.core.Flyway;
import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.CrsRecordRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.CrsRecordRepositoryBean;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt.LoginAttemptRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt.LoginAttemptRepositoryBean;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.spring4.JdbiFactoryBean;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;

import static java.util.Collections.singletonList;

@Configuration
@EnableTransactionManagement
@Order(1)
public class PassengerJDBIConfig {

    private static final Log logger = LogFactory.getLog(PassengerJDBIConfig.class);

    @Bean
    @Qualifier("passenger.datasource")
    @Primary
    @ConfigurationProperties("passenger.datasource")
    public HikariDataSource passengerDataSource() {
        final HikariDataSource dataSource = DataSourceBuilder.create().type(HikariDataSource.class).build();
        dataSource.setRegisterMbeans(true);
        dataSource.setPoolName("PassengerPool");
        return dataSource;
    }

    @Bean
    @Primary
    @Qualifier("passenger.db")
    public JdbiFactoryBean passengerDBI(@Qualifier("passenger.datasource") final HikariDataSource dataSource) {
        logConnection(dataSource);
        migrate("sql/migration/passenger", dataSource);
        final JdbiFactoryBean factoryBean = new JdbiFactoryBean(dataSource);
        factoryBean.setAutoInstallPlugins(true);
        factoryBean.setPlugins(singletonList(new SqlObjectPlugin()));
        return factoryBean;
    }

    @Bean
    @Qualifier("audit.public")
    public AuditService auditService(@Qualifier("passenger.db") final Jdbi jdbi, final MeterRegistry meterRegistry) {
        return new AuditService(jdbi, meterRegistry);
    }

    @Bean
    public DataSourceTransactionManager passengerDatasourceTransactionManager(@Qualifier("passenger.datasource") final DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public LoginAttemptRepository loginAttemptRepository(@Qualifier("passenger.db") final Jdbi jdbi) {
        return new LoginAttemptRepositoryBean(jdbi);
    }

    @Bean
    public CrsRecordRepository crsRecordRepository(@Qualifier("passenger.db") final Jdbi jdbi) {
        return new CrsRecordRepositoryBean(jdbi);
    }

    private void migrate(final String migrationsPath, DataSource dataSource) {
        Flyway flyway = new Flyway();
        flyway.setLocations(migrationsPath);
        flyway.setDataSource(dataSource);
        flyway.migrate();
    }

    private void logConnection(final DataSource dataSource) {
        try {
            logger.info("Datasource: " + dataSource.getConnection().getMetaData().getURL());
        }
        catch (final SQLException e) {
            logger.error("Unable to get connection from data source.", e);
        }
    }

}
