package pro.sky.telegrambot.integration.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import pro.sky.telegrambot.model.Adoption;
import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.model.enums.PetType;
import pro.sky.telegrambot.service.AdoptionService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
@Import(AdoptionService.class)
class AdoptionServiceIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AdoptionService adoptionService;

    private User user;
    private Pet pet;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setChatId(123L);
        user.setFirstName("Integration");
        user = entityManager.persistAndFlush(user);

        pet = new Pet();
        pet.setPetName("TestPet");
        pet.setPetType(PetType.DOG);
        pet = entityManager.persistAndFlush(pet);
    }

    @Test
    void extendProbation_shouldExtend14DaysAndUpdateStatus() {
        Adoption adoption = Adoption.builder()
                .user(user)
                .pet(pet)
                .startDate(LocalDate.now())
                .probationEndDate(LocalDate.now().plusDays(30))
                .probationStatus(Adoption.ProbationStatus.IN_PROGRESS)
                .build();
        Adoption saved = entityManager.persistAndFlush(adoption);

        adoptionService.extendProbation(saved.getId(), 14);

        Adoption updated = entityManager.find(Adoption.class, saved.getId());
        assertThat(updated.getProbationStatus()).isEqualTo(Adoption.ProbationStatus.EXTENDED_14);
        assertThat(updated.getProbationEndDate()).isEqualTo(LocalDate.now().plusDays(44));
    }

    @Test
    void extendProbation_shouldExtend30DaysAndUpdateStatus() {
        Adoption adoption = Adoption.builder()
                .user(user)
                .pet(pet)
                .probationEndDate(LocalDate.now().plusDays(30))
                .probationStatus(Adoption.ProbationStatus.IN_PROGRESS)
                .build();
        Adoption saved = entityManager.persistAndFlush(adoption);

        adoptionService.extendProbation(saved.getId(), 30);

        Adoption updated = entityManager.find(Adoption.class, saved.getId());
        assertThat(updated.getProbationStatus()).isEqualTo(Adoption.ProbationStatus.EXTENDED_30);
        assertThat(updated.getProbationEndDate()).isEqualTo(LocalDate.now().plusDays(60));
    }

    @Test
    void passProbation_shouldSetPassedAndGraduateUser() {
        user.setUserStatus(User.UserStatus.ADOPTER);
        entityManager.persist(user);
        Adoption adoption = Adoption.builder()
                .user(user)
                .pet(pet)
                .probationStatus(Adoption.ProbationStatus.IN_PROGRESS)
                .build();
        Adoption saved = entityManager.persistAndFlush(adoption);

        adoptionService.passProbation(saved.getId());

        Adoption updated = entityManager.find(Adoption.class, saved.getId());
        User updatedUser = entityManager.find(User.class, user.getId());
        assertThat(updated.getProbationStatus()).isEqualTo(Adoption.ProbationStatus.PASSED);
        assertThat(updatedUser.getUserStatus()).isEqualTo(User.UserStatus.GRADUATED);
    }

    @Test
    void failProbation_shouldSetFailed() {
        Adoption adoption = Adoption.builder()
                .user(user)
                .pet(pet)
                .probationStatus(Adoption.ProbationStatus.IN_PROGRESS)
                .build();
        Adoption saved = entityManager.persistAndFlush(adoption);

        adoptionService.failProbation(saved.getId());

        Adoption updated = entityManager.find(Adoption.class, saved.getId());
        assertThat(updated.getProbationStatus()).isEqualTo(Adoption.ProbationStatus.FAILED);
    }

    @Test
    void getActiveAdoptions_shouldReturnOnlyActiveStatuses() {
        Adoption inProgress = Adoption.builder().user(user).pet(pet).probationStatus(Adoption.ProbationStatus.IN_PROGRESS).build();
        Adoption extended14 = Adoption.builder().user(user).pet(pet).probationStatus(Adoption.ProbationStatus.EXTENDED_14).build();
        Adoption extended30 = Adoption.builder().user(user).pet(pet).probationStatus(Adoption.ProbationStatus.EXTENDED_30).build();
        Adoption passed = Adoption.builder().user(user).pet(pet).probationStatus(Adoption.ProbationStatus.PASSED).build();
        Adoption failed = Adoption.builder().user(user).pet(pet).probationStatus(Adoption.ProbationStatus.FAILED).build();
        entityManager.persist(inProgress);
        entityManager.persist(extended14);
        entityManager.persist(extended30);
        entityManager.persist(passed);
        entityManager.persist(failed);
        entityManager.flush();

        List<Adoption> active = adoptionService.getActiveAdoptions();

        assertThat(active).hasSize(3);
        assertThat(active).extracting(Adoption::getProbationStatus)
                .containsExactlyInAnyOrder(
                        Adoption.ProbationStatus.IN_PROGRESS,
                        Adoption.ProbationStatus.EXTENDED_14,
                        Adoption.ProbationStatus.EXTENDED_30);
    }

    @Test
    void findByProbationEndDateBefore_shouldReturnExpired() {
        Adoption expired = Adoption.builder()
                .user(user).pet(pet)
                .probationEndDate(LocalDate.now().minusDays(1))
                .build();
        Adoption notExpired = Adoption.builder()
                .user(user).pet(pet)
                .probationEndDate(LocalDate.now().plusDays(5))
                .build();
        entityManager.persist(expired);
        entityManager.persist(notExpired);
        entityManager.flush();

        List<Adoption> result = adoptionService.findByProbationEndDateBefore(LocalDate.now());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProbationEndDate()).isBefore(LocalDate.now());
    }

    @Test
    void extendProbation_adoptionNotFound_throwsException() {
        assertThatThrownBy(() -> adoptionService.extendProbation(999L, 14))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Усыновление не найдено");
    }
}
