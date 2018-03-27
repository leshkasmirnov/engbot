package ru.asmirnov.engbot.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import javax.annotation.PostConstruct;

/**
 * Long pooling bot registrator. Register bot in telegram API.
 *
 * Created by Alexey Smirnov 27.03.2018.
 */
@Component
@SuppressWarnings("unused")
public class LongPoolingBotRegistrator {

    private static final Logger LOGGER = LoggerFactory.getLogger(LongPoolingBotRegistrator.class);

    private final TelegramLongPollingBot longPollingBot;

    @Autowired
    public LongPoolingBotRegistrator(TelegramLongPollingBot longPollingBot) {
        this.longPollingBot = longPollingBot;
    }

    @PostConstruct
    protected void setUp() throws TelegramApiRequestException {
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            botsApi.registerBot(longPollingBot);
            LOGGER.info("Bot successfully registered");
        } catch (TelegramApiException e) {
            LOGGER.error("Unable to register bot", e);
            throw e;
        }
    }
}
