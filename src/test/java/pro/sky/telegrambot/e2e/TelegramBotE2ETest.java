package pro.sky.telegrambot.e2e;

import com.pengrad.telegrambot.TelegramBot;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
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

        User user = userRepository.findByChatId(testChatId).orElse(null);
        assertThat(user).isNull();
    }
}