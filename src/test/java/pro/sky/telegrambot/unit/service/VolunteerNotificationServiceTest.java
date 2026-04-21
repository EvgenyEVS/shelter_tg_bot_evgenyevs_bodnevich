package pro.sky.telegrambot.unit.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.model.Adoption;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.service.UserService;
import pro.sky.telegrambot.service.VolunteerNotificationService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VolunteerNotificationServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private TelegramBot telegramBot;

    @InjectMocks
    private VolunteerNotificationService volunteerNotificationService;

    @Test
    void notifyVolunteer_shouldSendMessageToAllVolunteers() {
        Long userChatId = 123L;
        User user = new User();
        user.setChatId(userChatId);
        user.setFirstName("Test");
        user.setTelegramUserName("@test");

        User volunteer1 = new User();
        volunteer1.setChatId(111L);
        volunteer1.setVolunteer(true);

        User volunteer2 = new User();
        volunteer2.setChatId(222L);
        volunteer2.setVolunteer(true);

        when(userService.getVolunteers()).thenReturn(List.of(volunteer1, volunteer2));

        volunteerNotificationService.notifyVolunteer(userChatId, user);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot, times(2)).execute(captor.capture());

        List<SendMessage> messages = captor.getAllValues();
        assertThat(messages).hasSize(2);
        assertThat(messages.get(0).getParameters().get("chat_id")).isEqualTo(111L);
        assertThat(messages.get(1).getParameters().get("chat_id")).isEqualTo(222L);
        assertThat(messages.get(0).getParameters().get("text").toString())
                .contains("Пользователь Test (@test) вызывает волонтёра");
    }

    @Test
    void notifyAboutMissedReports_shouldSendNotificationToAllVolunteers() {
        User user = new User();
        user.setChatId(123L);
        user.setFirstName("Petr");
        user.setTelegramUserName("@petr");

        User volunteer = new User();
        volunteer.setChatId(999L);
        volunteer.setVolunteer(true);

        when(userService.getVolunteers()).thenReturn(List.of(volunteer));

        volunteerNotificationService.notifyAboutMissedReports(user);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(captor.capture());

        SendMessage message = captor.getValue();
        assertThat(message.getParameters().get("chat_id")).isEqualTo(999L);
        String text = message.getParameters().get("text").toString();
        assertThat(text).contains("Усыновитель Petr (@petr) не присылает отчёты более 2 дней");
    }

    @Test
    void notifyForDecision_shouldSendMessageToAllVolunteers() {
        User user = new User();
        user.setChatId(123L);
        user.setFirstName("Anna");

        Adoption adoption = Adoption.builder()
                .id(1L)
                .user(user)
                .build();

        User volunteer = new User();
        volunteer.setChatId(888L);
        volunteer.setVolunteer(true);

        when(userService.getVolunteers()).thenReturn(List.of(volunteer));

        volunteerNotificationService.notifyForDecision(adoption);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(captor.capture());

        SendMessage message = captor.getValue();
        assertThat(message.getParameters().get("chat_id")).isEqualTo(888L);
        String text = message.getParameters().get("text").toString();
        assertThat(text).contains("Испытательный срок для пользователя Anna истёк. Требуется решение");
    }

    @Test
    void notifyVolunteer_noVolunteers_shouldNotSendAnyMessage() {
        when(userService.getVolunteers()).thenReturn(List.of());

        volunteerNotificationService.notifyVolunteer(123L, new User());

        verify(telegramBot, never()).execute(any(SendMessage.class));
    }
}
