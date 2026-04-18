package pro.sky.telegrambot.dto.shelterDto;

import pro.sky.telegrambot.model.enums.PetType;

public record ShelterResponseDto(
        Long id,
        PetType petType,
        String address,
        String shelterInfo,
        String shelterSchedule,
        String routeSchemaUrl,
        String contacts,
        String safetyPrecautionsAtShelter
) {
}
