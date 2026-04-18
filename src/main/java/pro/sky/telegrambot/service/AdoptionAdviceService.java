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
        AdoptionInfo info = adoptionInfoRepository.findByPetType(petType).orElseThrow(() -> new EntityNotFoundException("Никаких советов для: "+ petType));
        switch (adviceType) {
            case "before": return info.getAdviceBefore();
            case "documents": return info.getDocumentSet();
            case "transport": return info.getAdviceTransport();
            case "home_child": return info.getAdviceHomeForChild();
            case "home_adult": return info.getAdviceHomeForAdult();
            case "home_disabled": return info.getAdviceHomeForDisabilities();
            case "refusal": return info.getRefusalSet();
            default: return "Информация не найдена";
        }
    }

    public String getDogSpecificAdvice(String type) {
        AdoptionInfoDog dogInfo = (AdoptionInfoDog) adoptionInfoRepository.findByPetType(PetType.DOG).orElseThrow();
        if ("communication".equals(type)) return dogInfo.getDogPrimaryCommunication();
        if ("trainers".equals(type)) return dogInfo.getDogTrainers();
        return "";
    }

    public String getCatSpecificAdvice(String type) {
        AdoptionInfoCat catInfo = (AdoptionInfoCat) adoptionInfoRepository.findByPetType(PetType.CAT).orElseThrow();
        if ("communication".equals(type)) return catInfo.getCatPrimaryCommunication();
        if ("trainers".equals(type)) return catInfo.getCatTrainers();
        return "";
    }
}
