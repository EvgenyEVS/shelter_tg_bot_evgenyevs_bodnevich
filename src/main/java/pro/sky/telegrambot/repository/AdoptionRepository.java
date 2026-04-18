package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.Adoption;
import pro.sky.telegrambot.model.User;

import java.util.List;
import java.util.Optional;

public interface AdoptionRepository extends JpaRepository<Adoption, Long> {
    Optional<Adoption> findByUserAndProbationStatus(User user, Adoption.ProbationStatus status);
    List<Adoption> findByProbationStatus(Adoption.ProbationStatus status);
}
