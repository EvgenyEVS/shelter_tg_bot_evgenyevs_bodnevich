package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.service.MessageHandler;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;
    private final TelegramFacade telegramFacade;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, MessageHandler messageHandler, TelegramFacade telegramFacade) {
        this.telegramBot = telegramBot;
        this.telegramFacade = telegramFacade;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            try {
                telegramFacade.processUpdate(update);
                } catch (Exception e) {
                    logger.error("Error processing update ", e);
                }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
