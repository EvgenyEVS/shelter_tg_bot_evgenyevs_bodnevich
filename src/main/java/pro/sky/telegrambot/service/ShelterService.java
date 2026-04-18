package pro.sky.telegrambot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.dto.shelterDto.ShelterCreateDto;
import pro.sky.telegrambot.model.Shelter;
import pro.sky.telegrambot.model.enums.PetType;
import pro.sky.telegrambot.repository.ShelterRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShelterService {

    private final ShelterRepository shelterRepository;

//    public ShelterService(ShelterRepository shelterRepository) {
//        this.shelterRepository = shelterRepository;
//    }

    public Shelter createShelter(ShelterCreateDto createDto) {

        Shelter shelter = new Shelter();
        shelter.setPetType(convertToPetType(createDto.petType()));
        shelter.setAddress(createDto.address());
        shelter.setShelterInfo(createDto.shelterInfo());
        shelter.setShelterSchedule(createDto.shelterSchedule());
        shelter.setRouteSchemaUrl(createDto.routeSchemaUrl());
        shelter.setContacts(createDto.contacts());
        shelter.setSafetyPrecautionsAtShelter(createDto.safetyPrecautionsAtShelter());

        return shelterRepository.save(shelter);
    }


    public Shelter findShelterByPetType(PetType petType) {
        return shelterRepository.findShelterByPetType(petType).orElseThrow(
                () -> new EntityNotFoundException("Приют для " + petType + " не найден"));
    }

    public List<Shelter> allShelters () {
        return shelterRepository.findAll();
    }



    private PetType convertToPetType(String petTypeString) {
        if (petTypeString == null || petTypeString.isBlank()) {
            return PetType.UNKNOWN;
        }

        try {
            return PetType.valueOf(petTypeString.toUpperCase());
        } catch (IllegalArgumentException e) {
            return PetType.UNKNOWN;
        }
    }

}