package pro.sky.telegrambot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.telegrambot.dto.PetDto;
import pro.sky.telegrambot.mapper.PetMapper;
import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.model.Shelter;
import pro.sky.telegrambot.model.enums.PetType;
import pro.sky.telegrambot.repository.PetRepository;
import pro.sky.telegrambot.repository.ShelterRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class PetService {

    private final PetRepository petRepository;
    private final PetMapper petMapper;
    private final ShelterRepository shelterRepository;

    public PetService(PetRepository petRepository, PetMapper petMapper, ShelterRepository shelterRepository) {
        this.petRepository = petRepository;
        this.petMapper = petMapper;
        this.shelterRepository = shelterRepository;
    }

    public Pet addPet(PetDto petDto) {
        Pet pet = new Pet();
        petMapper.updatePetFromDto(petDto, pet);
        return petRepository.save(pet);
    }

    @Transactional(readOnly = true)
    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Pet> getPetById(Long id) {
        return petRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Pet> getCats() {
        return petRepository.findByPetType(PetType.CAT);
    }

    @Transactional(readOnly = true)
    public List<Pet> getDogs() {
        return petRepository.findByPetType(PetType.DOG);
    }

    @Transactional(readOnly = true)
    public List<Pet> getUnknown() {
        return petRepository.findByPetType(PetType.UNKNOWN);
    }

    public Pet updatePet(Long id, PetDto dto) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        petMapper.updatePetFromDto(dto, pet);
        return petRepository.save(pet);
    }

    public void deletePet(Long id) {
        if (!petRepository.existsById(id)) {
            throw new EntityNotFoundException("Питомец с ID = " + id + " не найден");
        }
        petRepository.deleteById(id);
    }

    public Pet automaticallyAssignShelterByPetType(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Питомец с ID = " + id + " не найден"));

        if (pet.getPetType() == null || pet.getPetType() == PetType.UNKNOWN) {
            log.info("Питомец '{}' не добавлен в приют: PetType = {}", pet.getPetName(), pet.getPetType());
        } else {
            Shelter shelter = shelterRepository.findShelterByPetType(pet.getPetType())
                    .orElseThrow(() -> new IllegalStateException("Приют для " + pet.getPetType() + " не найден"));
            pet.setShelter(shelter);
            log.info("Питомец '{}' добавлен в приют '{}'", pet.getPetName(), pet.getShelter());
        }
        return petRepository.save(pet);
    }
}
