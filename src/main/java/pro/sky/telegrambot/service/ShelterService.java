package pro.sky.telegrambot.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.dto.shelterDto.ShelterContactsDto;
import pro.sky.telegrambot.dto.shelterDto.ShelterCreateDto;
import pro.sky.telegrambot.dto.shelterDto.ShelterGeneralInfoDto;
import pro.sky.telegrambot.dto.shelterDto.ShelterResponseDto;
import pro.sky.telegrambot.mapper.ShelterMapper;
import pro.sky.telegrambot.model.Shelter;
import pro.sky.telegrambot.model.enums.PetType;
import pro.sky.telegrambot.repository.ShelterRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShelterService {

    private static final Logger log = LoggerFactory.getLogger(ShelterService.class);
    private final ShelterRepository shelterRepository;
    private final ShelterMapper shelterMapper;

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

    public ShelterResponseDto updateShelter (Long id, ShelterCreateDto createDto) {
        Shelter shelter = shelterRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Приют с ID = " + id + " не найден в БД.")
        );
        shelterMapper.updateShelterFromDto(createDto, shelter);
        Shelter saved = shelterRepository.save(shelter);
        return shelterMapper.toResponseDto(saved);
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

    public Shelter getShelterById (Long id) {
        return shelterRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Приют с ID = " + id + " не найден в БД")
        );
    }

    public boolean deleteShelterIfEmpty (Long id){
        Shelter shelter = shelterRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Приют с ID = " + id + " не найден в БД"));

        if(!shelter.getPets().isEmpty()) {
            log.info("Приют с " + id + " не удален, т.к. содержит питомцев. Сначала очистите приют");
            return false;
        }

        shelter.getPets().forEach(pet -> pet.setShelter(null));
        shelter.getPets().clear();

        shelterRepository.delete(shelter);
        log.info("Приют с " + id + " успешно удален");
        return true;
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