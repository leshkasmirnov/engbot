package ru.asmirnov.engbot;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.telegram.telegrambots.ApiContextInitializer;
import ru.asmirnov.engbot.config.AppConfig;

/**
 * Application entry point.
 *
 * Created by Alexey Smirnov 27.03.2018.
 */
public class App {

    public static void main(String[] args) {
        // ApiContext must be initialized here (before start spring context) otherwise BotSessionNotFound will occur
        ApiContextInitializer.init();
        new AnnotationConfigApplicationContext(AppConfig.class).start();
    }
}
