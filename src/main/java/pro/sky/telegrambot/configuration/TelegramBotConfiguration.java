package pro.sky.telegrambot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.DeleteMyCommands;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfiguration {

    @Value("${telegram.bot.token}")
    private String token;

    @Bean
    @ConditionalOnProperty(
            name = "telegram.bot.enabled",
            havingValue = "true",
            matchIfMissing = true
    )
    public TelegramBot telegramBot() {
        //TelegramBot bot = new TelegramBot(token);
        //bot.execute(new DeleteMyCommands()); - это сбрасывает меню команд закоментировано при Refactoring
        return new TelegramBot(token);
    }
}
