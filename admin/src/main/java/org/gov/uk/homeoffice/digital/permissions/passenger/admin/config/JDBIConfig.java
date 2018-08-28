package org.gov.uk.homeoffice.digital.permissions.passenger.admin.config;

import com.zaxxer.hikari.HikariDataSource;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.flywaydb.core.Flyway;
import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.spring4.JdbiFactoryBean;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.IdGenerator;
import org.springframework.util.JdkIdGenerator;

import javax.sql.DataSource;
import java.sql.SQLException;

import static java.util.Collections.singletonList;


@Configuration
@EnableTransactionManagement
public class JDBIConfig {

    private static final Log logger = LogFactory.getLog(JDBIConfig.class);

    @Bean
    @Qualifier("accounts.datasource")
    @ConfigurationProperties("accounts.datasource")
    @FlywayDataSource
    public HikariDataSource accountsDataSource() {
        final HikariDataSource dataSource =  DataSourceBuilder.create().type(HikariDataSource.class).build();
        dataSource.setPoolName("AccountsPool");
        return dataSource;
    }

    @Bean
    @Qualifier("accounts.db")
    public JdbiFactoryBean accountsDBI(@Qualifier("accounts.datasource") final HikariDataSource dataSource) {
        logConnection(dataSource);
        migrate("sql/migration/accounts", dataSource);
        final JdbiFactoryBean factoryBean = new JdbiFactoryBean(dataSource);
        factoryBean.setAutoInstallPlugins(true);
        factoryBean.setPlugins(singletonList(new SqlObjectPlugin()));

        return factoryBean;
    }

    @Bean
    public DataSourceTransactionManager accountsDatasourceTransactionManager(@Qualifier("accounts.datasource") final DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    @Qualifier("audit.admin")
    public AuditService adminAuditSerive(@Qualifier("accounts.db") final Jdbi jdbi, final MeterRegistry meterRegistry) {
        return new AuditService(jdbi, meterRegistry);
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

    @Bean
    public IdGenerator uuid() {
        return new JdkIdGenerator();
    }

}
