package pro.sky.telegrambot.dto.shelterDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ShelterCreateDto(

        @Schema(description = "Тип животного",
        example = "CAT",
        allowableValues = {"CAT", "DOG", "UNKNOW"})
        String petType,

        @Schema(description = "Адрес приюта", example = "улица Нуржол, 1")
        String address,

        @Schema(description = "Описание приюта")
        String shelterInfo,

        @Schema(description = "Часы работы приюта")
        String shelterSchedule,

        @Schema(description = "Схема проезда. Ссылка URL на картинку")
        String routeSchemaUrl,

        @Schema(description = "Контакты приюта")
        String contacts,

        @Schema(description = "Общие рекомендации о технике безопасности на территории приюта")
        String safetyPrecautionsAtShelter
) {
}