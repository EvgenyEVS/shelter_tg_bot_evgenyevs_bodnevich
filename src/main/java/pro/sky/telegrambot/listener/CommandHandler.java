package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.model.enums.PetType;
import pro.sky.telegrambot.service.ShelterInfoService;
import pro.sky.telegrambot.service.UserService;
import pro.sky.telegrambot.service.VolunteerNotificationService;

@Component
@RequiredArgsConstructor
public class CommandHandler {

    private final UserService userService;
    private final ShelterInfoService shelterInfoService;
    private final VolunteerNotificationService volunteerService;
    private final ReportFlowHandler reportFlowHandler;
    private final DialogStateManager dialogStateManager;
    private final KeyboardBuilder keyboardBuilder;

    public void handleMessage(Message message) {
        Long chatId = message.chat().id();
        User user = userService.getOrCreateUser(chatId, message.from());
        String text = message.text();

        if (user.getSelectedShelterType() == PetType.UNKNOWN) {
            dialogStateManager.handleFirstChoice(chatId, user, text);
            return;
        }

        if (text != null && text.startsWith("/")) {
            processCommand(chatId, user, text);
        } else if (text != null) {
            dialogStateManager.processState(user, text);
        } else if (message.photo() != null) {
            reportFlowHandler.handlePhoto(user, message);
        }
    }

    private void processCommand(Long chatId, User user, String command) {
        switch (command) {
            case Constants.COMMAND_START:
                if (user.getSelectedShelterType() != PetType.UNKNOWN) {
                    keyboardBuilder.sendMainMenu(chatId);
                } else {
                    dialogStateManager.askToChooseShelterType(chatId);
                }
                break;
            case Constants.COMMAND_SHELTER_INFO:
                dialogStateManager.askPetType(chatId, Constants.ACTION_SHELTER_INFO);
                break;
            case Constants.COMMAND_ADOPTION_ADVICE:
                dialogStateManager.askPetType(chatId, Constants.ACTION_ADOPTION_ADVICE);
                break;
            case Constants.COMMAND_SEND_REPORT:
                if (user.getUserStatus() == User.UserStatus.ADOPTER) {
                    reportFlowHandler.startReportFlow(chatId);
                } else {
                    keyboardBuilder.sendMessage(chatId, "❌ Эта функция доступна только усыновителям.");
                }
                break;
            case Constants.COMMAND_CALL_VOLUNTEER:
                volunteerService.notifyVolunteer(chatId, user);
                keyboardBuilder.sendMessage(chatId, "👨‍💼 Волонтёр уведомлён. Ожидайте ответа.");
                break;
            default:
                keyboardBuilder.sendMainMenu(chatId);
        }
    }
}
