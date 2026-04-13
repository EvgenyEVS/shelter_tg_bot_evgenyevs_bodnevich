package pro.sky.telegrambot.dto.shelterDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShelterGeneralInfoDto {
    private String shelterInfo;
    private String address;
}
