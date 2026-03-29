package pro.sky.telegrambot.dto;



import pro.sky.telegrambot.model.Pet;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;


public record CatCreateDto (

        @NotBlank(message = "Name не может быть пустым")
        @Size(min = 2, max = 30, message = "Name должно быть от 2 до 30 символов")
        String name,

        LocalDate birthDay,

        Pet.Gender gender
) {

}
