package pro.sky.telegrambot.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.dto.UserDto;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.repository.UserRepository;
import pro.sky.telegrambot.service.UserService;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getOrCreateUser_existing_returnsExisting() {
        Long chatId = 123L;
        com.pengrad.telegrambot.model.User tgUser = mock(com.pengrad.telegrambot.model.User.class);
        User existing = new User();
        existing.setChatId(chatId);
        when(userRepository.findByChatId(chatId)).thenReturn(Optional.of(existing));
        User result = userService.getOrCreateUser(chatId, tgUser);
        assertThat(result).isSameAs(existing);
        verify(userRepository, never()).save(any());
    }

    @Test
    void getOrCreateUser_new_createsAndSaves() {
        Long chatId = 123L;
        com.pengrad.telegrambot.model.User tgUser = mock(com.pengrad.telegrambot.model.User.class);
        when(tgUser.username()).thenReturn("user");
        when(tgUser.firstName()).thenReturn("First");
        when(tgUser.lastName()).thenReturn("Last");
        when(userRepository.findByChatId(chatId)).thenReturn(Optional.empty());
        User saved = new User();
        saved.setChatId(chatId);
        when(userRepository.save(any(User.class))).thenReturn(saved);
        User result = userService.getOrCreateUser(chatId, tgUser);
        assertThat(result.getChatId()).isEqualTo(chatId);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void getUserByChatId_notFound_throws() {
        when(userRepository.findByChatId(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() ->userService.getUserByChatId(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void updateUser_shouldMapAndSave() {
        Long id = 1L;
        User existing = new User();
        existing.setId(id);
        UserDto dto = new UserDto(123L, "tg", "Mark", "Doe", 30, "+7-999-123-44-55", User.UserStatus.ORDINARY);
        when(userRepository.findById(id)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenReturn(existing);
        User updated = userService.updateUser(id, dto);
        assertThat(updated.getFirstName()).isEqualTo("Mark");
        verify(userRepository).save(existing);
    }
}
