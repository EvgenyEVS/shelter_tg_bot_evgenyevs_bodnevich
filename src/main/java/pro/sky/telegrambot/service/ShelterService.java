package pro.sky.telegrambot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.telegrambot.dto.shelterDto.ShelterContactsDto;
import pro.sky.telegrambot.dto.shelterDto.ShelterCreateDto;
import pro.sky.telegrambot.dto.shelterDto.ShelterGeneralInfoDto;
import pro.sky.telegrambot.dto.shelterDto.ShelterResponseDto;
import pro.sky.telegrambot.exception.ShelterNotEmptyException;
import pro.sky.telegrambot.mapper.ShelterMapper;
import pro.sky.telegrambot.model.Shelter;
import pro.sky.telegrambot.model.enums.PetType;
import pro.sky.telegrambot.repository.ShelterRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShelterService {

    private final ShelterRepository shelterRepository;
    private final ShelterMapper shelterMapper;

    @Transactional
    @CacheEvict(value = {"shelterByType", "shelterGeneralInfo", "shelterContacts",
            "allShelters", "shelterById", "shelterInfo"},
            allEntries = true)
    public Shelter createShelter(ShelterCreateDto createDto) {
        Shelter shelter = shelterMapper.toEntity(createDto);
        shelter.setPetType(convertToPetType(createDto.petType()));
        return shelterRepository.save(shelter);
    }

    @Transactional
    @CacheEvict(value = {"shelterByType", "shelterGeneralInfo", "shelterContacts",
            "allShelters", "shelterById", "shelterInfo"},
            allEntries = true)
    public ShelterResponseDto updateShelter(Long id, ShelterCreateDto createDto) {
        Shelter shelter = shelterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Приют с ID " + id + " не найден"));
        shelterMapper.updateShelterFromDto(createDto, shelter);
        Shelter saved = shelterRepository.save(shelter);
        return shelterMapper.toResponseDto(saved);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "shelterByType", key = "#petType")
    public Shelter findShelterByPetType(PetType petType) {
        return shelterRepository.findShelterByPetType(petType)
                .orElseThrow(() -> new EntityNotFoundException("Приют для " + petType + " не найден"));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "allShelters", unless = "#result.isEmpty()")
    public List<ShelterResponseDto> allShelters() {
        return shelterRepository.findAll().stream()
                .map(shelterMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "shelterGeneralInfo", key = "#petType")
    public ShelterGeneralInfoDto getGeneralInfo(PetType petType) {
        Shelter shelter = findShelterByPetType(petType);
        return new ShelterGeneralInfoDto(shelter.getShelterInfo(), shelter.getAddress());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "shelterContacts", key = "#petType")
    public ShelterContactsDto getContacts(PetType petType) {
        Shelter shelter = findShelterByPetType(petType);
        return new ShelterContactsDto(shelter.getShelterSchedule(),
                shelter.getRouteSchemaUrl(),
                shelter.getContacts(),
                shelter.getSafetyPrecautionsAtShelter());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "shelterById", key = "#id")
    public Shelter getShelterById(Long id) {
        return shelterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Приют с ID " + id + " не найден"));
    }

    @Transactional
    @CacheEvict(value = {"shelterByType", "shelterGeneralInfo", "shelterContacts",
            "allShelters", "shelterById", "shelterInfo"},
            allEntries = true)
    public void deleteShelterIfEmpty(Long id) {
        Shelter shelter = getShelterById(id);
        if (!shelter.getPets().isEmpty()) {
            log.warn("Приют с ID {} не удалён, есть питомцы", id);
            throw new ShelterNotEmptyException("Приют содержит питомцев. Сначала очистите приют.");
        }
        shelterRepository.delete(shelter);
        log.info("Приют с ID {} удалён", id);
    }

    private PetType convertToPetType(String petTypeString) {
        if (petTypeString == null || petTypeString.isBlank()) return PetType.UNKNOWN;
        try {
            return PetType.valueOf(petTypeString.toUpperCase());
        } catch (IllegalArgumentException e) {
            return PetType.UNKNOWN;
        }
    }
}