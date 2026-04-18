package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.AdoptionInfo;
import pro.sky.telegrambot.model.enums.PetType;

import java.util.Optional;

public interface AdoptionInfoRepository extends JpaRepository<AdoptionInfo, Long> {
    Optional<AdoptionInfo> findByPetType(PetType petType);
}
