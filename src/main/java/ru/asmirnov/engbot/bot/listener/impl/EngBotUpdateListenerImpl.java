package ru.asmirnov.engbot.bot.listener.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.asmirnov.engbot.bot.listener.UpdateListener;
import ru.asmirnov.engbot.service.ReplyService;

/**
 * Eng bot update listener. Listen for new messages.
 * <p>
 * Created by Alexey Smirnov 27.03.2018.
 */
@Component
@SuppressWarnings("unused")
public class EngBotUpdateListenerImpl implements UpdateListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(EngBotUpdateListenerImpl.class);

    private final ReplyService replyService;

    public EngBotUpdateListenerImpl(ReplyService replyService) {
        this.replyService = replyService;
    }

    @Override
    public void onUpdateReceived(Update update, TelegramLongPollingBot telegramLongPollingBot) {
        LOGGER.info("{} received", update.toString());

        SendMessage sendMessage = replyService.reply(update.getMessage());
        LOGGER.info("Send {}", sendMessage);
        try {
            telegramLongPollingBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            LOGGER.error("Error!", e);
        }
    }
}
