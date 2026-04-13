package pro.sky.telegrambot.dto.shelterDto;

import lombok.*;
import pro.sky.telegrambot.model.enums.PetType;

@Data
@Builder (toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor

public class ShelterCreateDto {
    private String petType;
    private String shelterInfo;
    private String address;
    private String shelterSchedule;
    private String routeSchemaUrl;
    private String contacts;
    private String safetyPrecautionsAtShelter;
}
