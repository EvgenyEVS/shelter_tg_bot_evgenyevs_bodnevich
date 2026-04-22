package pro.sky.telegrambot.integration.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
//import pro.sky.telegrambot.config.TestJpaConfig;
import pro.sky.telegrambot.model.AdoptionInfo;
import pro.sky.telegrambot.model.AdoptionInfoCat;
import pro.sky.telegrambot.model.AdoptionInfoDog;
import pro.sky.telegrambot.model.enums.PetType;
import pro.sky.telegrambot.repository.AdoptionInfoRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
//@ContextConfiguration(classes = TestJpaConfig.class)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AdoptionInfoRepositoryTest {

    @Autowired
    private AdoptionInfoRepository adoptionInfoRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldPersistAndRetrieveCatSpecificFields() {
        AdoptionInfoCat cat = new AdoptionInfoCat();
        cat.setPetType(PetType.CAT);
        cat.setCatPrimaryCommunication("Meow");
        cat.setCatTrainers("Cat School");
        cat.setAdviceBefore("Prepare home");
        cat.setAdviceHomeForAdult("Safe environment");
        cat.setAdviceHomeForChild("Supervision required");
        cat.setAdviceHomeForDisabilities("Accessible home");
        cat.setAdviceTransport("In a carrier");
        cat.setDocumentSet("Passport, vet card");
        cat.setRefusalSet("Aggression, illness");
        entityManager.persistAndFlush(cat);

        AdoptionInfo found = adoptionInfoRepository.findByPetType(PetType.CAT).orElseThrow();
        assertThat(found).isInstanceOf(AdoptionInfoCat.class);
        AdoptionInfoCat catFound = (AdoptionInfoCat) found;
        assertThat(catFound.getCatPrimaryCommunication()).isEqualTo("Meow");
        assertThat(catFound.getCatTrainers()).isEqualTo("Cat School");
        assertThat(catFound.getAdviceBefore()).isEqualTo("Prepare home");
        assertThat(catFound.getAdviceHomeForAdult()).isEqualTo("Safe environment");
        assertThat(catFound.getAdviceHomeForChild()).isEqualTo("Supervision required");
        assertThat(catFound.getAdviceHomeForDisabilities()).isEqualTo("Accessible home");
        assertThat(catFound.getAdviceTransport()).isEqualTo("In a carrier");
        assertThat(catFound.getDocumentSet()).isEqualTo("Passport, vet card");
        assertThat(catFound.getRefusalSet()).isEqualTo("Aggression, illness");

    }

    @Test
    void shouldPersistAndRetrieveDogSpecificFields() {
        AdoptionInfoDog dog = new AdoptionInfoDog();
        dog.setPetType(PetType.DOG);
        dog.setDogPrimaryCommunication("Woof");
        dog.setDogTrainers("Dog Training Club");
        dog.setDocumentSet("Passport required");
        entityManager.persistAndFlush(dog);

        AdoptionInfo found = adoptionInfoRepository.findByPetType(PetType.DOG).orElseThrow();
        assertThat(found).isInstanceOf(AdoptionInfoDog.class);
        AdoptionInfoDog dogFound = (AdoptionInfoDog) found;
        assertThat(dogFound.getDogPrimaryCommunication()).isEqualTo("Woof");
        assertThat(dogFound.getDogTrainers()).isEqualTo("Dog Training Club");
        assertThat(dogFound.getDocumentSet()).isEqualTo("Passport required");
    }

    @Test
    void findByPetType_shouldReturnEmptyWhenNotFound() {
        assertThat(adoptionInfoRepository.findByPetType(PetType.UNKNOWN)).isEmpty();
    }
}