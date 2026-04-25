package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.dto.UserDto;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.service.ReportService;
import pro.sky.telegrambot.service.UserService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class ReportFlowHandler {

    private final UserService userService;
    private final ReportService reportService;
    private final KeyboardBuilder keyboardBuilder;
    private final Map<Long, ReportTempData> reportDataMap = new ConcurrentHashMap<>();

    public void startReportFlow(Long chatId) {
        keyboardBuilder.sendMessage(chatId, "📸 Пришлите фото животного.");
        User user = userService.getUserByChatId(chatId);
        user.setDialogState(User.UserDialogState.WAITING_REPORT_PHOTO);
        updateUser(user);
    }

    public void handlePhoto(User user, Message message) {
        Long chatId = user.getChatId();
        if (user.getDialogState() == User.UserDialogState.WAITING_REPORT_PHOTO) {
            String photoUrl = message.photo()[0].fileId();
            getData(chatId).setPhotoUrl(photoUrl);
            user.setDialogState(User.UserDialogState.WAITING_REPORT_DIET);
            updateUser(user);
            keyboardBuilder.sendMessage(chatId, "🍽 Опишите рацион животного:");
        } else {
            keyboardBuilder.sendMessage(chatId, "Используйте /send_report для отправки отчёта.");
        }
    }

    // Вызывается из CommandHandler или DialogStateManager при получении текста в состоянии WAITING_REPORT_DIET и т.д.
    public void handleText(User user, String text) {
        Long chatId = user.getChatId();
        switch (user.getDialogState()) {
            case WAITING_REPORT_DIET:
                getData(chatId).setDiet(text);
                user.setDialogState(User.UserDialogState.WAITING_REPORT_HEALTH);
                updateUser(user);
                keyboardBuilder.sendMessage(chatId, "🥗 Опишите общее самочувствие и привыкание к новому месту:");
                break;
            case WAITING_REPORT_HEALTH:
                getData(chatId).setHealth(text);
                user.setDialogState(User.UserDialogState.WAITING_REPORT_BEHAVIOR);
                updateUser(user);
                keyboardBuilder.sendMessage(chatId, "🐾 Опишите изменения в поведении (отказ от старых привычек, новые):");
                break;
            case WAITING_REPORT_BEHAVIOR:
                ReportTempData data = reportDataMap.remove(chatId);
                data.setBehavior(text);
                reportService.saveReport(chatId, data.getPhotoUrl(), data.getDiet(), data.getHealth(), data.getBehavior());
                user.setDialogState(User.UserDialogState.START);
                updateUser(user);
                keyboardBuilder.sendMessage(chatId, "✅ Отчёт сохранён. Спасибо!");
                break;
            default:
                keyboardBuilder.sendMessage(chatId, "Пожалуйста, выберите действие из меню.");
        }
    }

    private ReportTempData getData(Long chatId) {
        return reportDataMap.computeIfAbsent(chatId, k -> new ReportTempData());
    }

    private void updateUser(User user) {
        UserDto dto = new UserDto();
        dto.setChatId(user.getChatId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setUserStatus(user.getUserStatus());
        userService.updateUser(user.getId(), dto);
    }

    private static class ReportTempData {
        private String photoUrl, diet, health, behavior;
        public void setPhotoUrl(String url) { this.photoUrl = url; }
        public void setDiet(String d) { this.diet = d; }
        public void setHealth(String h) { this.health = h; }
        public void setBehavior(String b) { this.behavior = b; }
        public String getPhotoUrl() { return photoUrl; }
        public String getDiet() { return diet; }
        public String getHealth() { return health; }
        public String getBehavior() { return behavior; }
    }
}
