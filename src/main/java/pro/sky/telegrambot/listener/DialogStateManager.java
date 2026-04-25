package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.dto.UserDto;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.model.enums.PetType;
import pro.sky.telegrambot.service.ShelterInfoService;
import pro.sky.telegrambot.service.UserService;

@Component
@RequiredArgsConstructor
public class DialogStateManager {

    private final UserService userService;
    private final ShelterInfoService shelterInfoService;
    private final ReportFlowHandler reportFlowHandler;
    private final KeyboardBuilder keyboardBuilder;

    public void handleFirstChoice(Long chatId, User user, String text) {
        if (text.equals(Constants.CHOICE_CATS) || text.equals(Constants.CHOICE_DOGS)) {
            processShelterChoice(chatId, user, text);
        } else {
            askToChooseShelterType(chatId);
        }
    }

    public void askToChooseShelterType(Long chatId) {
        keyboardBuilder.sendShelterChoiceMenu(chatId);
    }

    private void processShelterChoice(Long chatId, User user, String choice) {
        PetType selectedType = choice.contains(Constants.CHOICE_CATS) ? PetType.CAT : PetType.DOG;
        user.setSelectedShelterType(selectedType);
        user.setDialogState(User.UserDialogState.START);
        updateUser(user);
        String msg = selectedType == PetType.CAT ?
                "Вы выбрали приют для кошек. Дальше можно узнать про приют больше и наконец-то стать достойным рабом..." :
                "Вы выбрали приют для собак. Дальше можно узнать про приют больше и наконец-то завести любимую собаку-барабаку!";
        keyboardBuilder.sendMessage(chatId, msg);
        keyboardBuilder.sendMainMenu(chatId);
    }

    public void processState(User user, String text) {
        Long chatId = user.getChatId();
        if (user.getDialogState() == User.UserDialogState.WAITING_PHONE) {
            if (text.matches(Constants.PHONE_REGEX)) {
                user.setPhoneNumber(text);
                user.setDialogState(User.UserDialogState.START);
                updateUser(user);
                keyboardBuilder.sendMessage(chatId, "✅ Номер сохранён.");
            } else {
                keyboardBuilder.sendMessage(chatId, "❌ Неверный формат. Используйте +7-9XX-XXX-XX-XX");
            }
        } else {
            // Другие состояния обрабатываются через ReportFlowHandler или перенаправляются
            keyboardBuilder.sendMessage(chatId, "Пожалуйста, выберите действие из меню.");
        }
    }

    public void askPetType(Long chatId, String action) {
        User user = userService.getUserByChatId(chatId);
        PetType selected = user.getSelectedShelterType();
        if (Constants.ACTION_SHELTER_INFO.equals(action)) {
            String info = shelterInfoService.getFullInfo(selected);
            keyboardBuilder.sendMessage(chatId, info);
        } else if (Constants.ACTION_ADOPTION_ADVICE.equals(action)) {
            keyboardBuilder.sendAdoptionMenu(chatId, selected);
        }
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
}
