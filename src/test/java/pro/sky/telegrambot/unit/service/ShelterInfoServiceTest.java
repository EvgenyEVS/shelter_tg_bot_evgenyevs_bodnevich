package pro.sky.telegrambot.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.dto.shelterDto.ShelterContactsDto;
import pro.sky.telegrambot.dto.shelterDto.ShelterGeneralInfoDto;
import pro.sky.telegrambot.model.Shelter;
import pro.sky.telegrambot.model.enums.PetType;
import pro.sky.telegrambot.service.ShelterInfoService;
import pro.sky.telegrambot.service.ShelterService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShelterInfoServiceTest {

    @Mock
    private ShelterService shelterService;

    @InjectMocks
    private ShelterInfoService shelterInfoService;

    @Test
    void getFullInfo_shouldReturnFormattedString() {
        // given
        PetType petType = PetType.CAT;
        Shelter shelter = new Shelter();
        shelter.setShelterInfo("Уютный приют для кошек");
        shelter.setAddress("ул. Кошачья, 5");

        ShelterGeneralInfoDto generalInfo = new ShelterGeneralInfoDto(
                shelter.getShelterInfo(),
                shelter.getAddress()
        );

        ShelterContactsDto contacts = new ShelterContactsDto(
                "10:00-18:00",
                "http://map.cat",
                "+7-777-111-22-33",
                "Не кормить с рук"
        );

        when(shelterService.findShelterByPetType(petType)).thenReturn(shelter);
        when(shelterService.getGeneralInfo(petType)).thenReturn(generalInfo);
        when(shelterService.getContacts(petType)).thenReturn(contacts);

        // when
        String result = shelterInfoService.getFullInfo(petType);

        // then
        String expected = "🏢 Уютный приют для кошек\n📍 Адрес: ул. Кошачья, 5\n🕒 Режим работы: 10:00-18:00\n📞 Контакты: +7-777-111-22-33\n🗺 Схема проезда: http://map.cat\n⚠️ Правила безопасности: Не кормить с рук";
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void getFullInfo_shouldWorkForDog() {
        // given
        PetType petType = PetType.DOG;
        Shelter shelter = new Shelter();
        shelter.setShelterInfo("Лучший приют для собак");
        shelter.setAddress("ул. Собачья, 10");

        ShelterGeneralInfoDto generalInfo = new ShelterGeneralInfoDto(
                shelter.getShelterInfo(),
                shelter.getAddress()
        );

        ShelterContactsDto contacts = new ShelterContactsDto(
                "09:00-20:00",
                "http://map.dog",
                "+7-888-222-33-44",
                "Держать собак на поводке"
        );

        when(shelterService.findShelterByPetType(petType)).thenReturn(shelter);
        when(shelterService.getGeneralInfo(petType)).thenReturn(generalInfo);
        when(shelterService.getContacts(petType)).thenReturn(contacts);

        // when
        String result = shelterInfoService.getFullInfo(petType);

        // then
        assertThat(result).contains("🏢 Лучший приют для собак");
        assertThat(result).contains("📍 Адрес: ул. Собачья, 10");
        assertThat(result).contains("🕒 Режим работы: 09:00-20:00");
        assertThat(result).contains("📞 Контакты: +7-888-222-33-44");
        assertThat(result).contains("🗺 Схема проезда: http://map.dog");
        assertThat(result).contains("⚠️ Правила безопасности: Держать собак на поводке");
    }
}
