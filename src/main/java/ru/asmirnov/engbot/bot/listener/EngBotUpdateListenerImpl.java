package ru.asmirnov.engbot.bot.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.Update;

/**
 * Eng bot update listener. Listen for new messages.
 *
 * Created by Alexey Smirnov 27.03.2018.
 */
@Component
@SuppressWarnings("unused")
public class EngBotUpdateListenerImpl implements UpdateListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(EngBotUpdateListenerImpl.class);

    @Override
    public void onUpdateReceived(Update update) {
        LOGGER.info(update.toString());
    }
}
