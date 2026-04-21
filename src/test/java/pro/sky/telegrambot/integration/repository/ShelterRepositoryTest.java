package pro.sky.telegrambot.integration.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
//import pro.sky.telegrambot.config.TestJpaConfig;
import pro.sky.telegrambot.model.Shelter;
import pro.sky.telegrambot.model.enums.PetType;
import pro.sky.telegrambot.repository.ShelterRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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
        catShelter.setContacts("+7-777-777-77-77");
        catShelter.setRouteSchemaUrl("http://map.cat");
        catShelter.setShelterInfo("Cat shelter info");
        catShelter.setShelterSchedule("10:00-18:00");
        catShelter.setSafetyPrecautionsAtShelter("Be quiet");

        Shelter dogShelter = new Shelter();
        dogShelter.setPetType(PetType.DOG);
        dogShelter.setAddress("Dog Avenue");
        dogShelter.setContacts("+7-888-888-88-88");
        dogShelter.setRouteSchemaUrl("http://map.dog");
        dogShelter.setShelterInfo("Dog shelter info");
        dogShelter.setShelterSchedule("09:00-19:00");
        dogShelter.setSafetyPrecautionsAtShelter("Keep distance");

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
        shelter.setRouteSchemaUrl("http://map.dog");           // Обязательное поле!
        shelter.setShelterInfo("Dog shelter info");            // Обязательное поле!
        shelter.setShelterSchedule("10:00-18:00");             // Обязательное поле!
        shelter.setSafetyPrecautionsAtShelter("Be careful");   // Обязательное поле!

        Shelter saved = shelterRepository.save(shelter);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getPetType()).isEqualTo(PetType.DOG);
        assertThat(saved.getAddress()).isEqualTo("123 Dog St");
        assertThat(saved.getContacts()).isEqualTo("+7-777-777-77-77");
        assertThat(saved.getRouteSchemaUrl()).isEqualTo("http://map.dog");
    }
}