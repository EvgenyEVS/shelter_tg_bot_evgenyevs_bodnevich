package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.model.enums.PetType;
import pro.sky.telegrambot.service.ShelterInfoService;

@Component
@RequiredArgsConstructor
public class CallbackQueryHandler {

    private final ShelterInfoService shelterInfoService;
    private final KeyboardBuilder keyboardBuilder;
    private final TelegramBot telegramBot;

    public void handleCallback(CallbackQuery callback) {
        Long chatId = callback.message().chat().id();
        String data = callback.data();

        if (data.startsWith(Constants.CALLBACK_SHELTER_INFO_PREFIX)) {
            PetType type = PetType.valueOf(data.split("_")[2]);
            String info = shelterInfoService.getFullInfo(type);
            keyboardBuilder.sendMessage(chatId, info);
        } else if (data.startsWith(Constants.CALLBACK_ADOPTION_ADVICE_PREFIX)) {
            PetType type = PetType.valueOf(data.split("_")[2]);
            keyboardBuilder.sendAdoptionMenu(chatId, type);
        }

        telegramBot.execute(new AnswerCallbackQuery(callback.id()));
    }
}
