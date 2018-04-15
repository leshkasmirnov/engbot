package ru.asmirnov.engbot.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import ru.asmirnov.engbot.bot.listener.UpdateListener;

/**
 * Eng bot. Use long pooling for received messages.
 *
 * Created by Alexey Smirnov 27.03.2018.
 */
@Component
@SuppressWarnings("unused")
public class EngBot extends TelegramLongPollingBot {

    private final String botToken;
    private final String userName;
    private final UpdateListener updateListener;

    @Autowired
    public EngBot(@Value("${engbot.token}") String botToken,
                  @Value("${engbot.username:EngHelper}")String userName,
                  UpdateListener updateListener) {
        this.botToken = botToken;
        this.userName = userName;
        this.updateListener = updateListener;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        updateListener.onUpdateReceived(update, this);
    }

    @Override
    public String getBotUsername() {
        return userName;
    }
}
