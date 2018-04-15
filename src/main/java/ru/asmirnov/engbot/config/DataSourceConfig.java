package ru.asmirnov.engbot.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author Alexey Smirnov at 15/04/2018
 */
@Configuration
public class DataSourceConfig {

    private final String url;
    private final String username;
    private final String password;
    private final String connectionTestQuery;
    private final int minIdle;
    private final int maxPoolSize;

    public DataSourceConfig(@Value("${engbot.datasource.url}") String url,
                            @Value("${engbot.datasource.username}") String user,
                            @Value("${engbot.datasource.password}") String password,
                            @Value("${engbot.datasource.tst.query}") String connectionTestQuery,
                            @Value("${engbot.datasource.pool.min.idle:3}") int minIdle,
                            @Value("${engbot.datasource.pool.max.size:10}") int maxPoolSize) {
        this.url = url;
        this.username = user;
        this.password = password;
        this.connectionTestQuery = connectionTestQuery;
        this.minIdle = minIdle;
        this.maxPoolSize = maxPoolSize;
    }

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setConnectionTestQuery(connectionTestQuery);
        config.setConnectionInitSql(connectionTestQuery);
        config.setMinimumIdle(minIdle);
        config.setMaximumPoolSize(maxPoolSize);

        return new HikariDataSource(config);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
