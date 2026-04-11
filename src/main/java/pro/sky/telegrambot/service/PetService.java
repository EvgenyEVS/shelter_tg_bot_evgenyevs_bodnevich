package pro.sky.telegrambot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.dto.PetDto;
import pro.sky.telegrambot.mapper.PetMapper;
import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.model.enums.PetType;
import pro.sky.telegrambot.repository.PetRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PetService {

    private final PetRepository petRepository;
    private final PetMapper petMapper;

    @Autowired
    public PetService(PetRepository petRepository, PetMapper petMapper) {
        this.petRepository = petRepository;
        this.petMapper = petMapper;
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

}
