package pro.sky.telegrambot.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.model.Adoption;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.repository.AdoptionRepository;
import pro.sky.telegrambot.service.AdoptionService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdoptionServiceTest {

    @Mock
    private AdoptionRepository adoptionRepository;

    @InjectMocks
    private AdoptionService adoptionService;

    @Test
    void extendProbation_14days_shouldUpdateStatusAndEndDate() {
        Long adoptionId = 1L;
        Adoption adoption = Adoption.builder()
                .id(adoptionId)
                .probationStatus(Adoption.ProbationStatus.IN_PROGRESS)
                .probationEndDate(LocalDate.now().plusDays(30))
                .build();
        when(adoptionRepository.findById(adoptionId)).thenReturn(Optional.of(adoption));
        adoptionService.extendProbation(adoptionId, 14);
        assertThat(adoption.getProbationStatus()).isEqualTo(Adoption.ProbationStatus.EXTENDED_14);
        assertThat(adoption.getProbationEndDate()).isEqualTo(LocalDate.now().plusDays(44));
        verify(adoptionRepository).save(adoption);
    }

    @Test
    void passProbation_shouldSetPassedAndGraduateuser() {
        Long adoptionId = 1L;
        User user = new User();
        user.setUserStatus(User.UserStatus.ADOPTER);
        Adoption adoption = Adoption.builder().id(adoptionId).user(user).build();
        when(adoptionRepository.findById(adoptionId)).thenReturn(Optional.of(adoption));
        adoptionService.passProbation(adoptionId);
        assertThat(adoption.getProbationStatus()).isEqualTo(Adoption.ProbationStatus.PASSED);
        assertThat(user.getUserStatus()).isEqualTo(User.UserStatus.GRADUATED);
        verify(adoptionRepository).save(adoption);
    }

    @Test
    void failProbation_shouldSetFailed() {
        Long adoptionId = 1L;
        Adoption adoption = Adoption.builder().id(adoptionId).build();
        when(adoptionRepository.findById(adoptionId)).thenReturn(Optional.of(adoption));
        adoptionService.failProbation(adoptionId);
        assertThat(adoption.getProbationStatus()).isEqualTo(Adoption.ProbationStatus.FAILED);
        verify(adoptionRepository).save(adoption);
    }

    @Test
    void extendProbation_notFound_throwException() {
        when(adoptionRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> adoptionService.extendProbation(99L, 14))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
