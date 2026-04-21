package pro.sky.telegrambot.unit.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pro.sky.telegrambot.dto.PetDto;
import pro.sky.telegrambot.mapper.PetMapper;
import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.model.enums.Gender;
import pro.sky.telegrambot.model.enums.PetStatus;
import pro.sky.telegrambot.model.enums.PetType;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class PetMapperTest {

    private final PetMapper petMapper = Mappers.getMapper(PetMapper.class);

    @Test
    void updatePetFromDto_shouldMapFields() {
        PetDto dto = new PetDto(1L, PetType.CAT, "Murzik", LocalDate.now(), Gender.MALE, true, PetStatus.AVAILABLE, "Fluffy cat", "Healthy", "None");
        Pet pet = new Pet();
        petMapper.updatePetFromDto(dto, pet);

        assertThat(pet.getPetName()).isEqualTo("Murzik");
        assertThat(pet.getPetDescription()).isEqualTo("Fluffy cat");
        assertThat(pet.getOwner()).isNull();
    }
}
