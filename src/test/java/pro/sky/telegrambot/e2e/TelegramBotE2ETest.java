package pro.sky.telegrambot.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.TelegramBot;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import pro.sky.telegrambot.dto.UserDto;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("e2etest")
class TelegramBotE2ETest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private TelegramBot telegramBot;

    @Test
    void fullUserJourney_shelterChoiceAndReport() throws Exception {
        Long testChatId = 999L;

        UserDto userDto = new UserDto(testChatId, "@reporter", "Rep", "Ort",
                25, "+7-999-555-44-33", User.UserStatus.ADOPTER);

        // Выводим DTO для отладки
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("Sending userDto: " + mapper.writeValueAsString(userDto));

        // Получаем ответ как строку, чтобы увидеть ошибку
        ResponseEntity<String> createResponse = restTemplate.postForEntity("/users", userDto, String.class);
        System.out.println("Response status: " + createResponse.getStatusCode());
        System.out.println("Response body: " + createResponse.getBody());

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Если дошли сюда, парсим ответ
        if (createResponse.getStatusCode() == HttpStatus.CREATED) {
            User user = mapper.readValue(createResponse.getBody(), User.class);
            assertThat(user).isNotNull();
            assertThat(user.getFirstName()).isEqualTo("Rep");
        }
    }
}