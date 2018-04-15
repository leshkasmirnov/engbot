package ru.asmirnov.engbot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.telegram.telegrambots.ApiContextInitializer;
import ru.asmirnov.engbot.config.AppConfig;

/**
 * Application entry point.
 *
 * Created by Alexey Smirnov 27.03.2018.
 */
public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        LOGGER.info("Telegram api context initialization...");
        // ApiContext must be initialized here (before start spring context) otherwise BotSessionNotFound will occur
        ApiContextInitializer.init();
        LOGGER.info("Telegram api context initialization OK.");
        LOGGER.info("Spring context starting...");
        new AnnotationConfigApplicationContext(AppConfig.class).start();
        LOGGER.info("Spring context starting OK.");
        LOGGER.info("Application started.");
    }
}
