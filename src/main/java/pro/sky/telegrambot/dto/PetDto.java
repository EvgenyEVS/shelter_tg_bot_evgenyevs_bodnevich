package pro.sky.telegrambot.dto;


import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.model.enums.Gender;
import pro.sky.telegrambot.model.enums.PetType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;


public record PetDto(

        @NotBlank(message = "Обязательно при создании определить тип питомца (например, CAT или DOG)")
        PetType petType,

        @NotBlank(message = "Name не может быть пустым")
        @Size(min = 2, max = 30, message = "Name должно быть от 2 до 30 символов")
        String name,

        @Past(message = "BirthDay не может быть из будущего")
        LocalDate birthDay,

        Gender gender
) {
}
