package pro.sky.telegrambot.integration.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
//import pro.sky.telegrambot.config.TestJpaConfig;
import pro.sky.telegrambot.model.Adoption;
import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.model.enums.PetType;
import pro.sky.telegrambot.repository.AdoptionRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
//@ContextConfiguration(classes = TestJpaConfig.class)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AdoptionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AdoptionRepository adoptionRepository;

    @Test
    void findByProbationStatus_shouldReturnFiltered() {
        User user = new User();
        user.setChatId(123L);
        entityManager.persist(user);
        Pet pet = new Pet();
        pet.setPetType(PetType.CAT);
        entityManager.persist(pet);

        Adoption active = Adoption.builder()
                .user(user).pet(pet).probationStatus(Adoption.ProbationStatus.IN_PROGRESS)
                .probationEndDate(LocalDate.now().plusDays(10)).build();
        Adoption passed = Adoption.builder()
                .user(user).pet(pet).probationStatus(Adoption.ProbationStatus.PASSED).build();
        entityManager.persist(active);
        entityManager.persist(passed);
        entityManager.flush();

        List<Adoption> result = adoptionRepository.findByProbationStatus(Adoption.ProbationStatus.IN_PROGRESS);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProbationStatus()).isEqualTo(Adoption.ProbationStatus.IN_PROGRESS);
    }

    @Test
    void findByProbationEndDateBefore_shouldReturnExpired() {
        User user = new User();
        user.setChatId(123L);
        entityManager.persist(user);
        Pet pet = new Pet();
        entityManager.persist(pet);
        Adoption expired = Adoption.builder()
                .user(user).pet(pet).probationEndDate(LocalDate.now().minusDays(1)).build();
        entityManager.persist(expired);
        entityManager.flush();

        List<Adoption> result = adoptionRepository.findByProbationEndDateBefore(LocalDate.now());
        assertThat(result).hasSize(1);
    }
}
