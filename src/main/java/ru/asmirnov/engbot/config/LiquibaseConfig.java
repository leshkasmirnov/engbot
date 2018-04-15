package ru.asmirnov.engbot.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;

/**
 * @author Alexey Smirnov at 15/04/2018
 */
@Configuration
@Import(DataSourceConfig.class)
public class LiquibaseConfig {

    @Bean
    public SpringLiquibase springLiquibase(DataSource dataSource) {
        SpringLiquibase springLiquibase = new SpringLiquibase();
        springLiquibase.setDataSource(dataSource);
        springLiquibase.setDropFirst(false);
        springLiquibase.setChangeLog("db/liquibase/changelog/changelog-master.xml");

        return springLiquibase;
    }
}
