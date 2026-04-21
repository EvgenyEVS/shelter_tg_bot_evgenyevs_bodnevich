package pro.sky.telegrambot.unit.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import pro.sky.telegrambot.model.*;
import pro.sky.telegrambot.model.enums.PetType;
import pro.sky.telegrambot.repository.UserRepository;
import pro.sky.telegrambot.service.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MessageHandlerTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ShelterInfoService shelterInfoService;
    @Mock
    private AdoptionAdviceService adoptionAdviceService;
    @Mock
    private ReportService reportService;
    @Mock
    private VolunteerNotificationService volunteerNotificationService;
    @Mock
    private TelegramBot telegramBot;
    @Mock
    private UserService userService;

    @InjectMocks
    private MessageHandler messageHandler;

    @Test
    void handleMessage_newUser_asksToChooseShelter() {
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(chat.id()).thenReturn(123L);
        when(message.chat()).thenReturn(chat);

        User tgUser = mock(User.class);
        when(tgUser.id()).thenReturn(123L);
        when(tgUser.username()).thenReturn("user");
        when(tgUser.firstName()).thenReturn("First");
        when(tgUser.lastName()).thenReturn("Last");
        when(message.from()).thenReturn(tgUser);
        when(message.text()).thenReturn("/start");

        pro.sky.telegrambot.model.User appUser = new pro.sky.telegrambot.model.User();
        when(userService.getOrCreateUser(eq(123L), any())).thenReturn(appUser);

        messageHandler.handleMessage(message);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot, atLeastOnce()).execute(captor.capture());
        String text = captor.getValue().getParameters().get("text").toString();
        assertThat(text).contains("Для начала давайте выберем приют");
    }

    @Test
    void handleMessage_shelterChoice_setsPetType() {
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(chat.id()).thenReturn(123L);
        when(message.chat()).thenReturn(chat);
        when(message.text()).thenReturn("Кошки");

        pro.sky.telegrambot.model.User appUser = new pro.sky.telegrambot.model.User();
        appUser.setId(1L);
        appUser.setChatId(123L);
        appUser.setSelectedShelterType(PetType.UNKNOWN);
        when(userService.getOrCreateUser(eq(123L), any())).thenReturn(appUser);
        when(userService.updateUser(eq(1L), any(pro.sky.telegrambot.dto.UserDto.class)))
                .thenReturn(appUser);

        messageHandler.handleMessage(message);

        assertThat(appUser.getSelectedShelterType()).isEqualTo(PetType.CAT);
    }

    @Test
    void handleCallback_adoptionAdvice_callsSendAdoptionMenu() {
        CallbackQuery callback = mock(CallbackQuery.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(chat.id()).thenReturn(123L);
        when(message.chat()).thenReturn(chat);
        when(callback.message()).thenReturn(message);
        when(callback.data()).thenReturn("adoption_advice_CAT");

        messageHandler.handleCallback(callback);

        verify(telegramBot, atLeastOnce()).execute(any(SendMessage.class));
        verify(userService, never()).getOrCreateUser(anyLong(), any());

    }
}
