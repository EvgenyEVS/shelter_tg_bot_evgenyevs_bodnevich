package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.User;

@Service
@RequiredArgsConstructor
public class VolunteerNotificationService {
    private final UserService userService;
    private final TelegramBot telegramBot;

    public void notifyVolunteer(Long userChatId, User user) {
        userService.getVolunteers().forEach(vol -> {
            String msg = String.format("🆘 Пользователь %s (%s) вызывает волонтёра.", user.getFirstName(), user.getTelegramUserName());
            telegramBot.execute(new SendMessage(vol.getChatId(), msg));
        });
    }

    public void notifyAboutMissedReports(User user) {
        userService.getVolunteers().forEach(vol -> {
            String msg = String.format("⚠️ Усыновитель %s (%s) не присылает отчёты более 2 дней.", user.getFirstName(), user.getTelegramUserName());
            telegramBot.execute(new SendMessage(vol.getChatId(), msg));
        });
    }

    public void notifyForDecision(pro.sky.telegrambot.model.Adoption adoption) {
        userService.getVolunteers().forEach(vol -> {
            String msg = String.format("⚖️ Испытательный срок для пользователя %s истёк. Требуется решение.", adoption.getUser().getFirstName());
            telegramBot.execute(new SendMessage(vol.getChatId(), msg));
        });
    }
}
