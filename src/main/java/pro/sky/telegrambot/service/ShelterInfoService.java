package pro.sky.telegrambot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.dto.shelterDto.ShelterContactsDto;
import pro.sky.telegrambot.dto.shelterDto.ShelterGeneralInfoDto;
import pro.sky.telegrambot.model.Shelter;
import pro.sky.telegrambot.model.enums.PetType;

@Service
@RequiredArgsConstructor
public class ShelterInfoService {
    private final ShelterService shelterService;

    public String getFullInfo(PetType petType) {
        Shelter shelter = shelterService.findShelterByPetType(petType);
        ShelterGeneralInfoDto general = shelterService.getGeneralInfo(petType);
        ShelterContactsDto contacts = shelterService.getContacts(petType);
        return String.format("🏢 %s\n📍 Адрес: %s\n🕒 Режим работы: %s\n📞 Контакты: %s\n🗺 Схема проезда: %s\n⚠️ Правила безопасности: %s",
                general.shelterInfo(),
                general.address(),
                contacts.shelterSchedule(),
                contacts.contacts(),
                contacts.routeSchemaUrl(),
                contacts.safetyPrecautionsAtShelter());
    }
}
