package pro.sky.telegrambot.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.TelegramBot;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pro.sky.telegrambot.controller.UserController;
import pro.sky.telegrambot.dto.UserDto;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.service.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private TelegramBot telegramBot;

    @Test
    void createUser_shouldReturnCreated() throws Exception {
        UserDto dto = new UserDto(123L, "@test", "John", "Doe", 30, "+7-999-123-45-67", User.UserStatus.ORDINARY);
        User saved = new User();
        saved.setId(1L);
        saved.setChatId(123L);
        saved.setFirstName("John");
        when(userService.createUser(any(UserDto.class))).thenReturn(saved);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void createUser_invalidData_shouldReturnBadRequest() throws Exception {
        UserDto invalidDto = new UserDto(null, null, "", "", 16, "12345", null);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllUsers_shouldReturnList() throws Exception {
        User user1 = new User();
        user1.setId(1L);
        user1.setFirstName("Alice");
        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Bob");
        when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("Alice"))
                .andExpect(jsonPath("$[1].firstName").value("Bob"));
    }

    @Test
    void getUserById_shouldReturnUser() throws Exception {
        User user = new User();
        user.setId(5L);
        user.setFirstName("Charlie");
        when(userService.getUserById(5L)).thenReturn(user);

        mockMvc.perform(get("/users/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.firstName").value("Charlie"));
    }

    @Test
    void getUserByChatId_shouldReturnUser() throws Exception {
        User user = new User();
        user.setChatId(123456L);
        user.setFirstName("David");
        when(userService.getUserByChatId(123456L)).thenReturn(user);

        mockMvc.perform(get("/users/byChatId?chatId=123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatId").value(123456))
                .andExpect(jsonPath("$.firstName").value("David"));
    }

    @Test
    void updateUser_shouldReturnUpdated() throws Exception {
        UserDto dto = new UserDto(123L, "@new", "Updated", "Name", 25, "+7-999-888-77-66", User.UserStatus.ADOPTER);
        User updated = new User();
        updated.setId(10L);
        updated.setFirstName("Updated");
        when(userService.updateUser(eq(10L), any(UserDto.class))).thenReturn(updated);

        mockMvc.perform(put("/users/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"));
    }

    @Test
    void deleteUser_shouldReturnNoContent() throws Exception {
        doNothing().when(userService).deleteUser(99L);

        mockMvc.perform(delete("/users/99"))
                .andExpect(status().isNoContent());
        verify(userService, times(1)).deleteUser(99L);
    }
}
