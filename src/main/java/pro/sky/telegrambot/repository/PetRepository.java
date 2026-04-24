package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.model.enums.PetType;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    // Pet findCatById(Long id); - REFACTORING


    //List<Pet> id (Long id); - REFACTORING

    List<Pet> findByPetType(PetType petType);
    List<Pet> findByOwner(User owner);

}
