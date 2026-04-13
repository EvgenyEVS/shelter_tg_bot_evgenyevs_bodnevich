package pro.sky.telegrambot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.dto.shelterDto.ShelterContactsDto;
import pro.sky.telegrambot.dto.shelterDto.ShelterCreateDto;
import pro.sky.telegrambot.dto.shelterDto.ShelterGeneralInfoDto;
import pro.sky.telegrambot.model.Shelter;
import pro.sky.telegrambot.model.enums.PetType;
import pro.sky.telegrambot.repository.ShelterRepository;

import javax.persistence.EntityNotFoundException;
import javax.swing.text.html.parser.Entity;
import javax.validation.constraints.NotNull;
import java.util.EmptyStackException;
import java.util.Optional;

@Service
public class ShelterService {

    private final ShelterRepository shelterRepository;

    @Autowired
    public ShelterService(ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }

    public Shelter createShelter (ShelterCreateDto createDto) {
        Shelter shelter = new Shelter();

        if(createDto.getPetType() != null && !createDto.getPetType().isEmpty()){
        shelter.setPetType(PetType.valueOf(createDto.getPetType()));}
        else {shelter.setPetType(PetType.UNKNOWN);}

        shelter.setShelterInfo(createDto.getShelterInfo());
        shelter.setAddress(createDto.getAddress());
        shelter.setShelterSchedule(createDto.getShelterSchedule());
        shelter.setRouteSchemaUrl(createDto.getRouteSchemaUrl());
        shelter.setContacts(createDto.getContacts());
        shelter.setSafetyPrecautionsAtShelter(createDto.getSafetyPrecautionsAtShelter());

        return shelterRepository.save(shelter);
    }

    public Shelter getShelterByPetType(PetType petType) {
        return shelterRepository.findShelterByPetType(petType)
                .orElseThrow(()-> new EntityNotFoundException("Приют для" + petType + "не найден."));
    }

    public ShelterGeneralInfoDto getGeneralInfo(PetType petType) {
        Shelter shelter = shelterRepository.findShelterByPetType(petType).orElseThrow();
        return ShelterGeneralInfoDto.builder()
                .shelterInfo(shelter.getShelterInfo())
                .address(shelter.getAddress())
                .build();
    }

    public ShelterContactsDto getContacts(PetType petType){
        Shelter shelter = shelterRepository.findShelterByPetType(petType).orElseThrow();
        return ShelterContactsDto.builder()
                .shelterSchedule(shelter.getShelterSchedule())
                .routeSchemaUrl(shelter.getRouteSchemaUrl())
                .contacts(shelter.getContacts())
                .safetyPrecautionsAtShelter(shelter.getSafetyPrecautionsAtShelter())
                .build();
    }

}
