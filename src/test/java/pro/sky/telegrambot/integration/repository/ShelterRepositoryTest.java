package pro.sky.telegrambot.integration.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pro.sky.telegrambot.model.Shelter;
import pro.sky.telegrambot.model.enums.PetType;
import pro.sky.telegrambot.repository.ShelterRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class ShelterRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ShelterRepository shelterRepository;

    @Test
    void findShelterByPetType_shouldReturnCorrectShelter() {
        Shelter catShelter = new Shelter();
        catShelter.setPetType(PetType.CAT);
        catShelter.setAddress("Cat Street");
        Shelter dogShelter = new Shelter();
        dogShelter.setPetType(PetType.DOG);
        dogShelter.setAddress("Dog Avenue");
        entityManager.persist(catShelter);
        entityManager.persist(dogShelter);
        entityManager.flush();

        var found = shelterRepository.findShelterByPetType(PetType.CAT);
        assertThat(found).isPresent();
        assertThat(found.get().getAddress()).isEqualTo("Cat Street");
    }

    @Test
    void findShelterByPetType_shouldReturnEmptyWhenNotFound() {
        assertThat(shelterRepository.findShelterByPetType(PetType.UNKNOWN)).isEmpty();
    }

    @Test
    void save_shouldPersistShelter() {
        Shelter shelter = new Shelter();
        shelter.setPetType(PetType.DOG);
        shelter.setAddress("123 Dog St");
        shelter.setContacts("+7-777-777-77-77");
        Shelter saved = shelterRepository.save(shelter);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getPetType()).isEqualTo(PetType.DOG);
    }
}