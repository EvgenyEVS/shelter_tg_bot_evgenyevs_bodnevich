package pro.sky.telegrambot.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.dto.PetDto;
import pro.sky.telegrambot.mapper.PetMapper;
import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.model.enums.PetType;
import pro.sky.telegrambot.repository.PetRepository;
import pro.sky.telegrambot.service.PetService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private PetRepository petRepository;
    @Mock
    private PetMapper petMapper;

    @InjectMocks
    private PetService petService;

    @Test
    void addPet_shouldMapAndSave() {
        PetDto dto = new PetDto(null, PetType.CAT, "Nick", null, null, false, null, null, null, null);
        Pet pet = new Pet();
        when(petRepository.save(any(Pet.class))).thenReturn(pet);
        Pet result = petService.addPet(dto);
        verify(petMapper).updatePetFromDto(dto, pet);
        verify(petRepository).save(pet);
        assertThat(result).isSameAs(pet);
    }

    @Test
    void getCats_shouldReturnList() {
        Pet cat = new Pet();
        cat.setPetType(PetType.CAT);
        when(petRepository.findByPetType(PetType.CAT)).thenReturn(List.of(cat));
        List<Pet> cats = petService.getCats();
        assertThat(cats).hasSize(1);
        assertThat(cats.get(0).getPetType()).isEqualTo(PetType.CAT);
    }
}
