package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.Dog;

import java.util.List;

public interface DogRepository extends JpaRepository<Dog, Long> {

    Dog findDogById(Long id);
}
