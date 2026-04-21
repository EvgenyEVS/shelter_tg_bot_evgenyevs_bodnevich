package pro.sky.telegrambot.unit.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.listener.TelegramBotUpdatesListener;
import pro.sky.telegrambot.service.MessageHandler;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TelegramBotUpdatesListenerTest {

    @Mock
    private TelegramBot telegramBot;
    @Mock
    private MessageHandler messageHandler;

    @InjectMocks
    private TelegramBotUpdatesListener listener;

    @Test
    void process_shouldHandleMessage() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.message()).thenReturn(message);
        when(update.callbackQuery()).thenReturn(null);

        int result = listener.process(List.of(update));

        verify(messageHandler).handleMessage(message);
        assertThat(result).isEqualTo(UpdatesListener.CONFIRMED_UPDATES_ALL);
    }

    @Test
    void process_shouldHandleCallback() {
        Update update = mock(Update.class);
        CallbackQuery callback = mock(CallbackQuery.class);
        when(update.message()).thenReturn(null);
        when(update.callbackQuery()).thenReturn(callback);

        listener.process(List.of(update));

        verify(messageHandler).handleCallback(callback);
    }
}
