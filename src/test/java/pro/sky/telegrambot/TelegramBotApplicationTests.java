package pro.sky.telegrambot;

import com.pengrad.telegrambot.TelegramBot;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class TelegramBotApplicationTests {

	@MockBean
	private TelegramBot telegramBot;

	@Test
	void contextLoads() {
	}
}
