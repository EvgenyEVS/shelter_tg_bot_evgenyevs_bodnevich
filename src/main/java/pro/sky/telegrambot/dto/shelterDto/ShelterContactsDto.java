package pro.sky.telegrambot.dto.shelterDto;

public record ShelterContactsDto(
        String shelterSchedule,
        String routeSchemaUrl,
        String contacts,
        String safetyPrecautionsAtShelter) {
}
