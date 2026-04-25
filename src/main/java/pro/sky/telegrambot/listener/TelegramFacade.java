package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TelegramFacade {

    private final CommandHandler commandHandler;
    private final CallbackQueryHandler callbackQueryHandler;

    public void processUpdate(Update update) {
        if (update.message() != null) {
            commandHandler.handleMessage(update.message());
        } else if (update.callbackQuery() != null) {
            callbackQueryHandler.handleCallback(update.callbackQuery());
        }
    }
}
