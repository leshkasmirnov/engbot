package ru.asmirnov.engbot.bot.listener;

import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

/**
 * Abstract update listener.
 *
 * Created by Alexey Smirnov 27.03.2018.
 */
public interface UpdateListener {

    void onUpdateReceived(Update update, TelegramLongPollingBot telegramLongPollingBot);
}
