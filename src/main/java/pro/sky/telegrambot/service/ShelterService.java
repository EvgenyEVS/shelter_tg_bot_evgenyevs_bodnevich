package pro.sky.telegrambot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.dto.shelterDto.ShelterContactsDto;
import pro.sky.telegrambot.dto.shelterDto.ShelterCreateDto;
import pro.sky.telegrambot.dto.shelterDto.ShelterGeneralInfoDto;
import pro.sky.telegrambot.dto.shelterDto.ShelterResponseDto;
import pro.sky.telegrambot.model.Shelter;
import pro.sky.telegrambot.model.enums.PetType;
import pro.sky.telegrambot.repository.ShelterRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

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


    public List<ShelterResponseDto> allShelters() {
        return shelterRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


    public ShelterGeneralInfoDto getGeneralInfo(PetType petType) {
        Shelter shelter = shelterRepository.findShelterByPetType(petType)
                .orElseThrow(() -> new EntityNotFoundException("Приют для " + petType + " не найден"));
        return new ShelterGeneralInfoDto(
                shelter.getShelterInfo(), shelter.getAddress());
    }


    public ShelterContactsDto getContacts(PetType petType) {
        Shelter shelter = shelterRepository.findShelterByPetType(petType)
                .orElseThrow(() -> new EntityNotFoundException("Приют для " + petType + " не найден"));
        return new ShelterContactsDto(shelter.getShelterSchedule(),
                shelter.getRouteSchemaUrl(),
                shelter.getContacts(),
                shelter.getSafetyPrecautionsAtShelter());
    }


    //inner methods---------------------
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

    private ShelterResponseDto toDto(Shelter shelter) {
        return new ShelterResponseDto(shelter.getId(),
                shelter.getPetType(),
                shelter.getAddress(),
                shelter.getShelterInfo(),
                shelter.getShelterSchedule(),
                shelter.getRouteSchemaUrl(),
                shelter.getContacts(),
                shelter.getSafetyPrecautionsAtShelter());
    }

}