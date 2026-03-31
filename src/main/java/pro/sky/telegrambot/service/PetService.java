package pro.sky.telegrambot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.dto.PetDto;
import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.repository.PetRepository;

import java.util.List;

@Service
public class PetService {

    private final PetRepository petRepository;

    @Autowired
    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }


    /**
     * Creating Pat Entity and save in DB.
     * Validating data implemented in DTO.
     *
     * @param petDto
     * @return Cat (savedPet)
     */
    public Pet addPet(PetDto petDto) {

        Pet pet = new Pet();
        pet.setName(petDto.name());
        pet.setBirthDay(petDto.birthDay());
        pet.setGender(petDto.gender());

        Pet savedPet = petRepository.save(pet);
        return savedPet;
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
}
