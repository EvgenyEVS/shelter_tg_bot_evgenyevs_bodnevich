package pro.sky.telegrambot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.dto.PetDto;
import pro.sky.telegrambot.mapper.PetMapper;
import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.model.Shelter;
import pro.sky.telegrambot.model.enums.PetType;
import pro.sky.telegrambot.repository.PetRepository;
import pro.sky.telegrambot.repository.ShelterRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional

public class PetService {

    private final PetRepository petRepository;
    private final PetMapper petMapper;
    private final ShelterRepository shelterRepository;

    @Autowired
    public PetService(PetRepository petRepository, PetMapper petMapper, ShelterRepository shelterRepository) {
        this.petRepository = petRepository;
        this.petMapper = petMapper;
        this.shelterRepository = shelterRepository;
    }


    /**
     * Creating Pet Entity and save in DB.
     * Validating data implemented in DTO.
     *
     * @return Cat (savedPet)
     */
    public Pet addPet(PetDto petDto) {

        Pet pet = new Pet();
        petMapper.updatePetFromDto(petDto, pet);
        
        return petRepository.save(pet);
    }

    /**
     * Return List<Cat>, who contains all Cats saved in DB.
     * No parameters.
     *
     * @return List<Cat>
     */
    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public Optional<Pet> getPetById(Long id) {
        return petRepository.findById(id);
    }

    public List<Pet> getCats() {
        return petRepository.findByPetType(PetType.CAT);
    }

    public List<Pet> getDogs() {
        return petRepository.findByPetType(PetType.DOG);
    }

    public List<Pet> getUnknown() {
        return petRepository.findByPetType(PetType.UNKNOWN);
    }

    public Pet updatePet(Long id, PetDto dto) {

        Pet pet = petRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        petMapper.updatePetFromDto(dto, pet);
        return petRepository.save(pet);
    }

    public String deletePet (Long id){
        if (petRepository.existsById(id)) {
        petRepository.deleteById(id);
        return "Питомец с ID = " + id + " удален из БД";
        }else return "Питомец с ID = " + id + " не найден в БД";
    }

        public Pet automaticallyAssignShelterByPetType(Long id) {
        Pet pet = petRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Питомец с ID = " + id + "не найден в БД"));

        if (pet.getPetType() == null || pet.getPetType() == PetType.UNKNOWN) {
            log.info("Питомец с именем '{}' не может быть автоматически добавлен в приют, т.к. PetType =  '{}'",
                    pet.getPetName(),
                    pet.getPetType());
        } else {
            Shelter shelter = shelterRepository.findShelterByPetType(pet.getPetType())
                    .orElseThrow(() -> new IllegalStateException(
                            String.format("Приют для %s не найден", pet.getPetType())));
            pet.setShelter(shelter);
            log.info("Питомец с именем '{}' автоматически добавлен в приют '{}'.",
                    pet.getPetName(),
                    pet.getShelter());
        }
        return petRepository.save(pet);

    }

}
