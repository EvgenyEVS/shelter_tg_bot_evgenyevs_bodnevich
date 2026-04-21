package pro.sky.telegrambot.unit.model;

import org.junit.jupiter.api.Test;
import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.model.Shelter;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.model.enums.Gender;
import pro.sky.telegrambot.model.enums.PetStatus;
import pro.sky.telegrambot.model.enums.PetType;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PetTest {

    @Test
    void settersAndGetters_shouldWork() {
        Pet pet = new Pet();
        pet.setId(10L);
        pet.setPetType(PetType.DOG);
        pet.setPetName("Polkan");
        pet.setBirthDay(LocalDate.of(2024, 1, 11));
        pet.setGender(Gender.MALE);
        pet.setCastratedOrSpayed(true);
        pet.setPetStatus(PetStatus.AVAILABLE);
        pet.setPetDescription("Friendly dog");
        pet.setHealthInfo("Health");
        pet.setSpecialNeeds("None");
        Shelter shelter = new Shelter();
        pet.setShelter(shelter);
        User owner = new User();
        pet.setOwner(owner);

        assertThat(pet.getId()).isEqualTo(10L);
        assertThat(pet.getPetType()).isEqualTo(PetType.DOG);
        assertThat(pet.getPetName()).isEqualTo("Polkan");
        assertThat(pet.getBirthDay()).isEqualTo(LocalDate.of(2024, 1, 11));
        assertThat(pet.getGender()).isEqualTo(Gender.MALE);
        assertThat(pet.isCastratedOrSpayed()).isTrue();
        assertThat(pet.getPetStatus()).isEqualTo(PetStatus.AVAILABLE);
        assertThat(pet.getPetDescription()).isEqualTo("Friendly dog");
        assertThat(pet.getHealthInfo()).isEqualTo("Health");
        assertThat(pet.getSpecialNeeds()).isEqualTo("None");
        assertThat(pet.getShelter()).isEqualTo(shelter);
        assertThat(pet.getOwner()).isEqualTo(owner);
    }
}
