package pro.sky.telegrambot.unit.model;

import org.junit.jupiter.api.Test;
import pro.sky.telegrambot.model.Adoption;
import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.model.User;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AdoptionTest {

    @Test
    void builder_shouldCreateAdoptionWithDefaults() {
        User user = new User();
        Pet pet = new Pet();
        LocalDate startDate = LocalDate.now();

        Adoption adoption = Adoption.builder()
                .user(user)
                .pet(pet)
                .startDate(startDate)
                .build();

        assertThat(adoption.getUser()).isEqualTo(user);
        assertThat(adoption.getPet()).isEqualTo(pet);
        assertThat(adoption.getStartDate()).isEqualTo(startDate);
        assertThat(adoption.getMissedDays()).isZero();
        assertThat(adoption.getProbationStatus()).isEqualTo(Adoption.ProbationStatus.IN_PROGRESS);
        assertThat(adoption.getProbationEndDate()).isNull();
    }

    @Test
    void settersAndGetters_shouldWork() {
        Adoption adoption = new Adoption();
        adoption.setId(1L);
        adoption.setMissedDays(5);
        adoption.setProbationStatus(Adoption.ProbationStatus.EXTENDED_30);
        adoption.setProbationEndDate(LocalDate.now().plusDays(30));

        assertThat(adoption.getId()).isEqualTo(1L);
        assertThat(adoption.getMissedDays()).isEqualTo(5);
        assertThat(adoption.getProbationStatus()).isEqualTo(Adoption.ProbationStatus.EXTENDED_30);
        assertThat(adoption.getProbationEndDate()).isEqualTo(LocalDate.now().plusDays(30));
    }
}
