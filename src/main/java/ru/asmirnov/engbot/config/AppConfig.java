package ru.asmirnov.engbot.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Application configuration.
 *
 * Created by Alexey Smirnov 27.03.2018.
 */
@Configuration
@PropertySource("file:config/engbot.properties")
@ComponentScan("ru.asmirnov.engbot")
public class AppConfig {
}
