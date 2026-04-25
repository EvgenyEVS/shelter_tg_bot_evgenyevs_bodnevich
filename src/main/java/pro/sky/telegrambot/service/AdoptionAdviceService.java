package pro.sky.telegrambot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Adoption;
import pro.sky.telegrambot.model.AdoptionInfo;
import pro.sky.telegrambot.model.AdoptionInfoCat;
import pro.sky.telegrambot.model.AdoptionInfoDog;
import pro.sky.telegrambot.model.enums.PetType;
import pro.sky.telegrambot.repository.AdoptionInfoRepository;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class AdoptionAdviceService {
    private final AdoptionInfoRepository adoptionInfoRepository;

    public String getAdvice(PetType petType, String adviceType) {
        AdoptionInfo info = adoptionInfoRepository.findByPetType(petType)
                .orElseThrow(() -> new EntityNotFoundException("Никаких советов для: "+ petType));
        return switch (adviceType) {
            case "before" -> info.getAdviceBefore();
            case "documents" -> info.getDocumentSet();
            case "transport" -> info.getAdviceTransport();
            case "home_child" -> info.getAdviceHomeForChild();
            case "home_adult" -> info.getAdviceHomeForAdult();
            case "home_disabled" -> info.getAdviceHomeForDisabilities();
            case "refusal" -> info.getRefusalSet();
            default -> "Информация не найдена";
        };
    }

    public String getDogSpecificAdvice(String type) {
        AdoptionInfo info = adoptionInfoRepository.findByPetType(PetType.DOG)
                .orElseThrow(() -> new EntityNotFoundException("Нет информации для собак"));
        if (!(info instanceof AdoptionInfoDog dogInfo)) {
            throw new IllegalStateException("Неверный тип информации в БД для собак");
        }
        return "communication".equals(type) ? dogInfo.getDogPrimaryCommunication() :
                "trainers".equals(type) ? dogInfo.getDogTrainers() : "";
    }


    public String getCatSpecificAdvice(String type) {
        AdoptionInfo info = adoptionInfoRepository.findByPetType(PetType.DOG)
                .orElseThrow(() -> new EntityNotFoundException("Нет информации для кошек"));;
        if (!(info instanceof AdoptionInfoCat catInfo)) {
            throw new IllegalStateException("Неверный тип информации в БД для кошек");
        }
        return "communication".equals(type) ? catInfo.getCatPrimaryCommunication() :
                "trainers".equals(type) ? catInfo.getCatTrainers() : "";
    }
}
