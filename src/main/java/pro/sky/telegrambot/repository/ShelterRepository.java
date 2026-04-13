package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.Shelter;
import pro.sky.telegrambot.model.enums.PetType;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface ShelterRepository extends JpaRepository<Shelter, PetType> {

    Optional<Shelter> findShelterByPetType(PetType petType);
}
