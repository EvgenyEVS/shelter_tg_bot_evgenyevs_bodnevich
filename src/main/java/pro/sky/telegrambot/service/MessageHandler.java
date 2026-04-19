package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.mapstruct.control.MappingControl;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.dto.UserDto;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.model.enums.PetType;

@Service
@RequiredArgsConstructor
public class MessageHandler {
    private final UserService userService;
    private final ShelterInfoService shelterInfoService;
    private final AdoptionAdviceService adoptionAdviceService;
    private final ReportService reportService;
    private final VolunteerNotificationService volunteerService;
    private final TelegramBot telegramBot;

    private final java.util.Map<Long, ReportTempData> reportDataMap = new java.util.concurrent.ConcurrentHashMap<>();

    private void sendMessage(Long chatId, String text) {
        telegramBot.execute(new SendMessage(chatId, text));
    }

    private void sendMessage(Long chatId, String text, InlineKeyboardMarkup markup) {
        telegramBot.execute(new SendMessage(chatId, text).replyMarkup(markup));
    }

    private void sendMessage(Long chatId, String text, ReplyKeyboardMarkup markup) {
        telegramBot.execute(new SendMessage(chatId, text).replyMarkup(markup));
    }

    private void sendMainMenu(Long chatId) {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(
                new String[]{"🏢 Информация о приюте"},
                new String[]{"📋 Как взять животное"},
                new String[]{"📝 Прислать отчёт"},
                new String[]{"👨‍💼 Позвать волонтёра"}
        );
        keyboard.resizeKeyboard(true);
        sendMessage(chatId, "Выберите действие:", keyboard);
    }

    public void handleMessage(Message message) {
        Long chatId = message.chat().id();
        User user = userService.getOrCreateUser(chatId, message.from());
        String text = message.text();

        if (user.getSelectedShelterType() == PetType.UNKNOWN) {
            if (text != null && (text.equals("Кошки")) || (text.equals("Собаки"))) {
                processShelterChoice(chatId, user, text);
            } else {
                askToChooseShelterType(chatId);
            }
            return;
        }

        if (text != null && text.startsWith("/")) {
            processCommand(chatId, user, text);
        } else if (text != null) {
            processDialogState(chatId, user, text);
        } else if (message.photo() != null) {
            processPhoto(chatId, user, message);
        }
    }

    private void processCommand(Long chatId, User user, String command) {
        switch (command) {
            case "/start":
                //sendMainMenu(chatId);
                if (user.getSelectedShelterType() != PetType.UNKNOWN) {
                    sendMainMenu(chatId);
                } else {
                    askToChooseShelterType(chatId);
                }
                break;
            case "/shelter_info":
                askPetType(chatId, "shelter_info");
                break;
            case "/adoption_advice":
                askPetType(chatId, "adoption_advice");
                break;
            case "/send_report":
                if (user.getUserStatus() == User.UserStatus.ADOPTER) {
                    startReportFlow(chatId);
                } else {
                    sendMessage(chatId, "❌ Эта функция доступна только усыновителям.");
                }
                break;
            case "/call_volunteer":
                volunteerService.notifyVolunteer(chatId, user);
                sendMessage(chatId, "👨‍💼 Волонтёр уведомлён. Ожидайте ответа.");
                break;
            default:
                sendMainMenu(chatId);
        }
    }

    public void askToChooseShelterType(Long chatId) {
        String welcomeMessage = """
                Добро пожаловать! Я бот приюта для животных "Самый лучший приют Астаны".
                
                Далее я смогу Вам помочь:
                * дать общую информацию о приюте
                * подобрать питомца, помочь с документами и усыновлением
                * отправлять отчеты и помогать стать счастливее)))
                
                Для начала давайте выберем приют. Кто для Вас интереснее? :
                """;
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(
                new String[]{"Кошки"},
                new String[]{"Собаки"}
        );
        keyboardMarkup.resizeKeyboard(true);
        sendMessage(chatId, welcomeMessage, keyboardMarkup);
    }

    private void processShelterChoice(Long chatId, User user, String choice) {
        PetType selectedPetType;

        if (choice.contains("Кошки")) {
            selectedPetType = PetType.CAT;
            sendMessage(chatId, "Вы выбрали приют для кошек. \n" +
                    "Дальше можно узнать про приют больше и наконец-то стать достойным рабом, \n " +
                    "чтобы служить великолепной гордой кошке!");
        } else {
            selectedPetType = PetType.DOG;
            sendMessage(chatId, "Вы выбрали приют для собак. \n" +
                    "Дальше можно узнать про приют больше и наконец-то завести любимую собаку-барабаку!");
        }
        user.setSelectedShelterType(selectedPetType);
        user.setDialogState(User.UserDialogState.START);
        userService.updateUser(user.getId(), convertToDto(user));
    }

    private void processDialogState(Long chatId, User user, String text) {
        switch (user.getDialogState()) {
            case WAITING_PHONE:
                if (text.matches("^\\+7-9\\d{2}-\\d{3}-\\d{2}-\\d{2}$")) {
                    user.setPhoneNumber(text);
                    user.setDialogState(User.UserDialogState.START);
                    userService.updateUser(user.getId(), convertToDto(user));
                    sendMessage(chatId, "✅ Номер сохранён.");
                } else {
                    sendMessage(chatId, "❌ Неверный формат. Используйте +7-9XX-XXX-XX-XX");
                }
                break;
            case WAITING_REPORT_DIET:
                reportDataMap.computeIfAbsent(chatId, k -> new ReportTempData()).setDiet(text);
                user.setDialogState(User.UserDialogState.WAITING_REPORT_HEALTH);
                sendMessage(chatId, "🥗 Опишите общее самочувствие и привыкание к новому месту:");
                break;
            case WAITING_REPORT_HEALTH:
                reportDataMap.get(chatId).setHealth(text);
                user.setDialogState(User.UserDialogState.WAITING_REPORT_BEHAVIOR);
                sendMessage(chatId, "🐾 Опишите изменения в поведении (отказ от старых привычек, новые):");
                break;
            case WAITING_REPORT_BEHAVIOR:
                ReportTempData data = reportDataMap.remove(chatId);
                data.setBehavior(text);
                reportService.saveReport(chatId, data.getPhotoUrl(), data.getDiet(), data.getHealth(), data.getBehavior());
                user.setDialogState(User.UserDialogState.START);
                sendMessage(chatId, "✅ Отчёт сохранён. Спасибо!");
                break;
            default:
                sendMessage(chatId, "Пожалуйста, выберите действие из меню.");
        }
    }

    private void processPhoto(Long chatId, User user, Message message) {
        if (user.getDialogState() == User.UserDialogState.WAITING_REPORT_PHOTO) {
            String photoUrl = message.photo()[0].fileId(); // для простоты сохраняем file_id
            reportDataMap.computeIfAbsent(chatId, k -> new ReportTempData()).setPhotoUrl(photoUrl);
            user.setDialogState(User.UserDialogState.WAITING_REPORT_DIET);
            sendMessage(chatId, "🍽 Опишите рацион животного:");
        } else {
            sendMessage(chatId, "Пожалуйста, используйте команду /send_report для отправки отчёта.");
        }
    }

    private void startReportFlow(Long chatId) {
        sendMessage(chatId, "📸 Пришлите фото животного.");
        User user = userService.getUserByChatId(chatId);
        user.setDialogState(User.UserDialogState.WAITING_REPORT_PHOTO);
        userService.updateUser(user.getId(), convertToDto(user));
    }

    private void askPetType(Long chatId, String action) {
        User user = userService.getUserByChatId(chatId);
        PetType selectedPetType = user.getSelectedShelterType();

        if (action.equals("shelter_info")) {
            String info = shelterInfoService.getFullInfo(selectedPetType);
            sendMessage(chatId, info);
        } else if (action.equals("adoption_advice")) {
            sendAdoptionMenu(chatId, selectedPetType);
        }
    }

    public void handleCallback(com.pengrad.telegrambot.model.CallbackQuery callback) {
        Long chatId = callback.message().chat().id();
        String data = callback.data();
        if (data.startsWith("shelter_info_")) {
            PetType type = PetType.valueOf(data.split("_")[2]);
            String info = shelterInfoService.getFullInfo(type);
            sendMessage(chatId, info);
        } else if (data.startsWith("adoption_advice_")) {
            PetType type = PetType.valueOf(data.split("_")[2]);
            sendAdoptionMenu(chatId, type);
        }
        telegramBot.execute(new com.pengrad.telegrambot.request.AnswerCallbackQuery(callback.id()));
    }

    private void sendAdoptionMenu(Long chatId, PetType type) {
        com.pengrad.telegrambot.model.request.InlineKeyboardMarkup markup = new com.pengrad.telegrambot.model.request.InlineKeyboardMarkup(
                new com.pengrad.telegrambot.model.request.InlineKeyboardButton("📋 Список животных").callbackData("list_" + type),
                new com.pengrad.telegrambot.model.request.InlineKeyboardButton("📄 Документы").callbackData("documents_" + type),
                new com.pengrad.telegrambot.model.request.InlineKeyboardButton("🚗 Транспортировка").callbackData("transport_" + type),
                new com.pengrad.telegrambot.model.request.InlineKeyboardButton("🏠 Обустройство для щенка/котёнка").callbackData("home_child_" + type),
                new com.pengrad.telegrambot.model.request.InlineKeyboardButton("🏠 Обустройство для взрослого").callbackData("home_adult_" + type),
                new com.pengrad.telegrambot.model.request.InlineKeyboardButton("🦽 Обустройство для инвалида").callbackData("home_disabled_" + type),
                new com.pengrad.telegrambot.model.request.InlineKeyboardButton("❌ Причины отказа").callbackData("refusal_" + type)
        );
        if (type == PetType.DOG) {
            markup.addRow(new com.pengrad.telegrambot.model.request.InlineKeyboardButton("🐕 Советы кинолога").callbackData("communication_DOG"));
            markup.addRow(new com.pengrad.telegrambot.model.request.InlineKeyboardButton("👨‍🏫 Кинологи").callbackData("trainers_DOG"));
        } else if (type == PetType.CAT) {
            markup.addRow(new com.pengrad.telegrambot.model.request.InlineKeyboardButton("🐈 Советы фелинолога").callbackData("communication_CAT"));
            markup.addRow(new com.pengrad.telegrambot.model.request.InlineKeyboardButton("👨‍🏫 Фелинологи").callbackData("trainers_CAT"));
        }
        sendMessage(chatId, "Выберите интересующую информацию:", markup);
    }

    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setChatId(user.getChatId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setUserStatus(user.getUserStatus());
        return dto;
    }

    private static class ReportTempData {
        String photoUrl, diet, health, behavior;

        void setPhotoUrl(String url) {
            this.photoUrl = url;
        }

        void setDiet(String d) {
            this.diet = d;
        }

        void setHealth(String h) {
            this.health = h;
        }

        void setBehavior(String b) {
            this.behavior = b;
        }

        String getPhotoUrl() {
            return photoUrl;
        }

        String getDiet() {
            return diet;
        }

        String getHealth() {
            return health;
        }

        String getBehavior() {
            return behavior;
        }
    }
}
