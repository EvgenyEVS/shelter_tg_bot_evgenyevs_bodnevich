package pro.sky.telegrambot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pro.sky.telegrambot.model.User;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для создания/обновления пользователя")
public class UserDto {
    @Schema(description = "Telegram chat ID", example = "123456789", required = true)
    @NotNull(message = "Chat ID не может быть пустым")
    private Long chatId;

    @Schema(description = "Telegram username", example = "batmanov_nik")
    private String telegramUserName;

    @Schema(description = "Имя", example = "Петр")
    @NotBlank(message = "Имя обязательно для заполнения")
    @Size(min = 2, max = 50, message = "Имя должно содержать от 2 до 50 символов")
    private String firstName;

    @Schema(description = "Фамилия", example = "Иванов")
    @NotBlank(message = "Фамилия обязательна для заполнения")
    @Size(max = 50, message = "Фамилия не должна превышать более 50 символов")
    private String lastName;

    @Schema(description = "Возраст", example = "30")
    @Min(value = 18, message = "Возраст должен быть не менее 18 лет")
    @Max(value = 65, message = "Возраст должен быть не более 65")
    private Integer age;

    @Schema(description = "Номер телефона в формате +7-9XX-XXX-XX-XX", example = "+7-999-345-99-99")
    @NotBlank(message = "Телефон обязателен для заполнения")
    @Pattern(regexp = "^\\+7-9\\d{2}-\\d{3}-\\d{2}-\\d{2}$", message = "Телефон должен соответствовать формату: +7-9XX-XXX-XX-XX")
    private String phoneNumber;

    @Schema(description = "Статус пользователя", example = "ORDINARY")
    private User.UserStatus userStatus;
}
