package pro.sky.telegrambot.dto.shelterDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShelterContactsDto {
    private String shelterSchedule;
    private String routeSchemaUrl;
    private String contacts;
    private String safetyPrecautionsAtShelter;
}
