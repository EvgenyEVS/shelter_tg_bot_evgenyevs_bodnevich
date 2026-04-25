package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.model.enums.PetType;

@Component
@RequiredArgsConstructor
public class KeyboardBuilder {

    private final TelegramBot telegramBot;

    public void sendMessage(Long chatId, String text) {
        telegramBot.execute(new SendMessage(chatId, text));
    }

    public void sendMainMenu(Long chatId) {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(
                new String[]{Constants.MENU_SHELTER_INFO},
                new String[]{Constants.MENU_ADOPTION_ADVICE},
                new String[]{Constants.MENU_SEND_REPORT},
                new String[]{Constants.MENU_CALL_VOLUNTEER}
        );
        keyboard.resizeKeyboard(true);
        telegramBot.execute(new SendMessage(chatId, "Выберите действие:").replyMarkup(keyboard));
    }

    public void sendShelterChoiceMenu(Long chatId) {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(
                new String[]{Constants.CHOICE_CATS},
                new String[]{Constants.CHOICE_DOGS}
        );
        keyboard.resizeKeyboard(true);
        String welcome = "Добро пожаловать! Я бот приюта для животных. Пожалуйста, выберите приют:";
        telegramBot.execute(new SendMessage(chatId, welcome).replyMarkup(keyboard));
    }

    public void sendAdoptionMenu(Long chatId, PetType type) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(
                new InlineKeyboardButton("📋 Список животных").callbackData(Constants.CALLBACK_LIST + type),
                new InlineKeyboardButton("📄 Документы").callbackData(Constants.CALLBACK_DOCUMENTS + type),
                new InlineKeyboardButton("🚗 Транспортировка").callbackData(Constants.CALLBACK_TRANSPORT + type),
                new InlineKeyboardButton("🏠 Обустройство для щенка/котёнка").callbackData(Constants.CALLBACK_HOME_CHILD + type),
                new InlineKeyboardButton("🏠 Обустройство для взрослого").callbackData(Constants.CALLBACK_HOME_ADULT + type),
                new InlineKeyboardButton("🦽 Обустройство для инвалида").callbackData(Constants.CALLBACK_HOME_DISABLED + type),
                new InlineKeyboardButton("❌ Причины отказа").callbackData(Constants.CALLBACK_REFUSAL + type)
        );
        if (type == PetType.DOG) {
            markup.addRow(new InlineKeyboardButton("🐕 Советы кинолога").callbackData(Constants.CALLBACK_COMMUNICATION_DOG));
            markup.addRow(new InlineKeyboardButton("👨‍🏫 Кинологи").callbackData(Constants.CALLBACK_TRAINERS_DOG));
        } else if (type == PetType.CAT) {
            markup.addRow(new InlineKeyboardButton("🐈 Советы фелинолога").callbackData(Constants.CALLBACK_COMMUNICATION_CAT));
            markup.addRow(new InlineKeyboardButton("👨‍🏫 Фелинологи").callbackData(Constants.CALLBACK_TRAINERS_CAT));
        }
        telegramBot.execute(new SendMessage(chatId, "Выберите интересующую информацию:").replyMarkup(markup));
    }
}