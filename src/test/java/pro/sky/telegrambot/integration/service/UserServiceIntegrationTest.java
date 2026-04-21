package pro.sky.telegrambot.integration.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.telegrambot.dto.UserDto;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.service.UserService;

import javax.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    void getOrCreateUser_shouldReturnExisting() {
        Long chatId = 123L;
        com.pengrad.telegrambot.model.User tgUser = mock(com.pengrad.telegrambot.model.User.class);
        when(tgUser.id()).thenReturn(chatId);
        when(tgUser.username()).thenReturn("testuser");
        when(tgUser.firstName()).thenReturn("Test");
        when(tgUser.lastName()).thenReturn("User");

        User first = userService.getOrCreateUser(chatId, tgUser);
        User second = userService.getOrCreateUser(chatId, tgUser);

        assertThat(second.getId()).isEqualTo(first.getId());
    }

    @Test
    void getOrCreateUser_shouldCreateNew() {
        Long chatId = 999L;
        com.pengrad.telegrambot.model.User tgUser = mock(com.pengrad.telegrambot.model.User.class);
        when(tgUser.id()).thenReturn(chatId);
        when(tgUser.username()).thenReturn("newuser");
        when(tgUser.firstName()).thenReturn("New");
        when(tgUser.lastName()).thenReturn("User");

        User created = userService.getOrCreateUser(chatId, tgUser);

        assertThat(created.getChatId()).isEqualTo(chatId);
        assertThat(created.getTelegramUserName()).isEqualTo("newuser");
        assertThat(created.getFirstName()).isEqualTo("New");
        assertThat(created.getLastName()).isEqualTo("User");
        assertThat(created.getUserStatus()).isEqualTo(User.UserStatus.NON_ACTIVE);
    }

    @Test
    void createUser_shouldSave() {
        UserDto dto = new UserDto(111L, "@test", "John", "Doe", 25, "+7-999-123-45-67", User.UserStatus.ORDINARY);
        User saved = userService.createUser(dto);
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void getUserByChatId_shouldReturnUser() {
        UserDto dto = new UserDto(222L, "@find", "Find", "Me", 30, "+7-999-888-77-66", User.UserStatus.ADOPTER);
        userService.createUser(dto);
        User found = userService.getUserByChatId(222L);
        assertThat(found.getChatId()).isEqualTo(222L);
    }

    @Test
    void getUserByChatId_notFound_throws() {
        assertThatThrownBy(() -> userService.getUserByChatId(99999L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void updateUser_shouldChangeFields() {
        UserDto original = new UserDto(333L, "@old", "Old", "Name", 40, "+7-999-111-22-33", User.UserStatus.NON_ACTIVE);
        User created = userService.createUser(original);
        UserDto updateDto = new UserDto(333L, "@new", "New", "Name", 41, "+7-999-444-55-66", User.UserStatus.ORDINARY);
        User updated = userService.updateUser(created.getId(), updateDto);
        assertThat(updated.getFirstName()).isEqualTo("New");
        assertThat(updated.getAge()).isEqualTo(41);
    }

    @Test
    void deleteUser_shouldRemove() {
        UserDto dto = new UserDto(444L, "@delete", "Delete", "Me", 20, "+7-999-777-88-99", User.UserStatus.NON_ACTIVE);
        User created = userService.createUser(dto);
        userService.deleteUser(created.getId());
        assertThatThrownBy(() -> userService.getUserById(created.getId()))
                .isInstanceOf(EntityNotFoundException.class);
    }
}