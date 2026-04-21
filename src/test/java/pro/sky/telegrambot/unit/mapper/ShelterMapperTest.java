package pro.sky.telegrambot.unit.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pro.sky.telegrambot.dto.shelterDto.ShelterCreateDto;
import pro.sky.telegrambot.mapper.ShelterMapper;
import pro.sky.telegrambot.model.Shelter;

import static org.assertj.core.api.Assertions.assertThat;

class ShelterMapperTest {

    private final ShelterMapper shelterMapper = Mappers.getMapper(ShelterMapper.class);

    @Test
    void toEntity_shouldMapAllFields() {
        ShelterCreateDto dto = new ShelterCreateDto("DOG", "Address", "Info", "9-18", "http://map", "contacts", "safety");
        Shelter shelter = shelterMapper.toEntity(dto);

        assertThat(shelter.getAddress()).isEqualTo("Address");
        assertThat(shelter.getShelterInfo()).isEqualTo("Info");
    }

    @Test
    void updateShelterFromDto_shouldIgnoreId() {
        Shelter existing = new Shelter();
        existing.setId(100L);
        ShelterCreateDto dto = new ShelterCreateDto("CAT", "New Address", null, null, null, null, null);
        shelterMapper.updateShelterFromDto(dto, existing);

        assertThat(existing.getId()).isEqualTo(100L);
        assertThat(existing.getAddress()).isEqualTo("New Address");
    }
}