package pro.sky.telegrambot.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.model.AdoptionInfo;
import pro.sky.telegrambot.model.AdoptionInfoCat;
import pro.sky.telegrambot.model.AdoptionInfoDog;
import pro.sky.telegrambot.model.enums.PetType;
import pro.sky.telegrambot.repository.AdoptionInfoRepository;
import pro.sky.telegrambot.service.AdoptionAdviceService;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdoptionAdviceServiceTest {

    @Mock
    private AdoptionInfoRepository adoptionInfoRepository;

    @InjectMocks
    private AdoptionAdviceService adoptionAdviceService;

    @Test
    void getAdvice_shouldReturnCorrectText() {
        AdoptionInfo info = new AdoptionInfo();
        info.setAdviceBefore("Prepare a bed");
        when(adoptionInfoRepository.findByPetType(PetType.CAT)).thenReturn(Optional.of(info));
        String advice = adoptionAdviceService.getAdvice(PetType.CAT, "before");
        assertThat(advice).isEqualTo("Prepare a bed");
    }

    @Test
    void getAdvice_invalidType_returnsDefault() {
        AdoptionInfo info = new AdoptionInfo();
        when(adoptionInfoRepository.findByPetType(PetType.DOG)).thenReturn(Optional.of(info));
        String advice = adoptionAdviceService.getAdvice(PetType.DOG, "unknown");
        assertThat(advice).isEqualTo("Information not found");
    }

    @Test
    void getDogSpecificAdvice_returnsDogTrainers() {
        AdoptionInfoDog dogInfo = new AdoptionInfoDog();
        dogInfo.setDogTrainers("John Smith");
        when(adoptionInfoRepository.findByPetType(PetType.DOG)).thenReturn(Optional.of(dogInfo));
        String trainers = adoptionAdviceService.getDogSpecificAdvice("trainers");
        assertThat(trainers).isEqualTo("John Smith");
    }

    @Test
    void getCatSpecificAdvice_returnsCatCommunication() {
        AdoptionInfoCat catInfo = new AdoptionInfoCat();
        catInfo.setCatPrimaryCommunication("Meow");
        when(adoptionInfoRepository.findByPetType(PetType.CAT)).thenReturn(Optional.of(catInfo));
        String comm = adoptionAdviceService.getCatSpecificAdvice("communication");
        assertThat(comm).isEqualTo("Meow");
    }
}
