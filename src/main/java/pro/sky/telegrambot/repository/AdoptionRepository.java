package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.Adoption;
import pro.sky.telegrambot.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AdoptionRepository extends JpaRepository<Adoption, Long> {
    List<Adoption> findByProbationStatus(Adoption.ProbationStatus status);
    List<Adoption> findByProbationEndDateBefore(LocalDate date);
    List<Adoption> findByProbationStatusIn(List<Adoption.ProbationStatus> statuses);

    Optional<Adoption> findByUserAndProbationStatusIn(User user, List<Adoption.ProbationStatus> statuses);
}
