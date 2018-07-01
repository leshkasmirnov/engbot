package ru.asmirnov.engbot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

/**
 * Application entry point.
 * <p>
 * Created by Alexey Smirnov 27.03.2018.
 */
@SpringBootApplication
public class App implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        LOGGER.info("Telegram api context initialization...");
        // ApiContext must be initialized here (before start spring context) otherwise BotSessionNotFound will occur
        ApiContextInitializer.init();
        LOGGER.info("Telegram api context initialization OK.");

        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) {
    }
}
