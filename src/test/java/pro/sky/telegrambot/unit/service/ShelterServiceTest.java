package pro.sky.telegrambot.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.dto.shelterDto.ShelterCreateDto;
import pro.sky.telegrambot.dto.shelterDto.ShelterResponseDto;
import pro.sky.telegrambot.model.Shelter;
import pro.sky.telegrambot.model.enums.PetType;
import pro.sky.telegrambot.repository.ShelterRepository;
import pro.sky.telegrambot.service.ShelterService;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShelterServiceTest {

    @Mock
    private ShelterRepository shelterRepository;

    @InjectMocks
    private ShelterService shelterService;

    @Test
    void createShelter_shouldMapAndSave() {
        ShelterCreateDto dto = new ShelterCreateDto("CAT", "Address", "Info", "9-18", "http://map", "+7-999-123-44-55", "Safety");
        Shelter saved = new Shelter();
        saved.setId(1L);
        saved.setPetType(PetType.CAT);
        when(shelterRepository.save(any(Shelter.class))).thenReturn(saved);
        Shelter result = shelterService.createShelter(dto);
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getPetType()).isEqualTo(PetType.CAT);
    }

    @Test
    void findShelterByPetType_existing_returnsShelter() {
        Shelter shelter = new Shelter();
        shelter.setPetType(PetType.DOG);
        when(shelterRepository.findShelterByPetType(PetType.DOG)).thenReturn(Optional.of(shelter));
        Shelter found = shelterService.findShelterByPetType(PetType.DOG);
        assertThat(found.getPetType()).isEqualTo(PetType.DOG);
    }

    @Test
    void findShelterByPetType_notFound_throws() {
        when(shelterRepository.findShelterByPetType(PetType.CAT)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> shelterService.findShelterByPetType(PetType.CAT))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void allShelters_returnsDtoList() {
        Shelter shelter = new Shelter();
        shelter.setId(1L);
        when(shelterRepository.findAll()).thenReturn(List.of(shelter));
        List<ShelterResponseDto> dtos = shelterService.allShelters();
        assertThat(dtos).hasSize(1);
        assertThat(dtos.get(0).id()).isEqualTo(1L);
    }
}
