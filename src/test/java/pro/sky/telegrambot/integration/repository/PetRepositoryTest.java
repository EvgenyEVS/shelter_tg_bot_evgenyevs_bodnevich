package pro.sky.telegrambot.integration.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
//import pro.sky.telegrambot.config.TestJpaConfig;
import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.model.enums.PetType;
import pro.sky.telegrambot.repository.PetRepository;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
//@ContextConfiguration(classes = TestJpaConfig.class)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PetRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PetRepository petRepository;

    private User owner;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setChatId(123L);
        owner = entityManager.persistAndFlush(owner);
    }

    @Test
    void findByPetType_shouldReturnCats() {
        Pet cat = new Pet();
        cat.setPetType(PetType.CAT);
        cat.setPetName("Whiskers");
        Pet dog = new Pet();
        dog.setPetType(PetType.DOG);
        dog.setPetName("Rex");
        entityManager.persist(cat);
        entityManager.persist(dog);
        entityManager.flush();

        List<Pet> cats = petRepository.findByPetType(PetType.CAT);
        assertThat(cats).hasSize(1);
        assertThat(cats.get(0).getPetName()).isEqualTo("Whiskers");
    }

    @Test
    void findByOwner_shouldReturnPetsOwnedByUser() {
        Pet pet1 = new Pet();
        pet1.setOwner(owner);
        Pet pet2 = new Pet();
        pet2.setOwner(owner);
        Pet pet3 = new Pet();
        pet3.setOwner(null);
        entityManager.persist(pet1);
        entityManager.persist(pet2);
        entityManager.persist(pet3);
        entityManager.flush();

        List<Pet> owned = petRepository.findByOwner(owner);
        assertThat(owned).hasSize(2);
        assertThat(owned).allMatch(p -> p.getOwner() != null && p.getOwner().equals(owner));
    }

    @Test
    void findCatById_shouldReturnCat() {
        Pet cat = new Pet();
        cat.setPetType(PetType.CAT);
        cat.setPetName("Tom");
        Pet saved = entityManager.persistAndFlush(cat);

        Pet found = petRepository.findById(saved.getId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getPetType()).isEqualTo(PetType.CAT);
        assertThat(found.getPetName()).isEqualTo("Tom");
    }

    @Test
    void findById_shouldReturnPet() {
        Pet pet = new Pet();
        pet.setPetName("Charlie");
        Pet saved = entityManager.persistAndFlush(pet);

        var found = petRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getPetName()).isEqualTo("Charlie");
    }
}
