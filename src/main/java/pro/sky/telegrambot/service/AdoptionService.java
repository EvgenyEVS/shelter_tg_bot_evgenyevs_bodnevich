package pro.sky.telegrambot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.telegrambot.model.Adoption;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.repository.AdoptionRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdoptionService {
    private final AdoptionRepository adoptionRepository;

    public List<Adoption> getActiveAdoptions() {
        return adoptionRepository.findByProbationStatusIn(
                Arrays.asList(
                        Adoption.ProbationStatus.IN_PROGRESS,
                        Adoption.ProbationStatus.EXTENDED_14,
                        Adoption.ProbationStatus.EXTENDED_30
                )
        );
    }

    public List<Adoption> findByProbationEndDateBefore(LocalDate date) {
        return adoptionRepository.findByProbationEndDateBefore(date);
    }

    @Transactional
    public Adoption save(Adoption adoption) {
        return adoptionRepository.save(adoption);
    }

    @Transactional
    public void extendProbation(Long adoptionId, int days) {
        Adoption adoption = adoptionRepository.findById(adoptionId)
                .orElseThrow(() -> new EntityNotFoundException("Усыновление не найдено"));
        if (days == 14) {
            adoption.setProbationStatus(Adoption.ProbationStatus.EXTENDED_14);
        } else if (days == 30) {
            adoption.setProbationStatus(Adoption.ProbationStatus.EXTENDED_30);
        }
        adoption.setProbationEndDate(adoption.getProbationEndDate().plusDays(days));
        adoptionRepository.save(adoption);
    }

    @Transactional
    public void passProbation(Long adoptionId) {
        Adoption adoption = adoptionRepository.findById(adoptionId)
                .orElseThrow(() -> new EntityNotFoundException("Усыновление не найдено"));
        adoption.setProbationStatus(Adoption.ProbationStatus.PASSED);
        adoption.getUser().setUserStatus(User.UserStatus.GRADUATED);
        adoptionRepository.save(adoption);
    }

    @Transactional
    public void failProbation(Long adoptionId) {
        Adoption adoption = adoptionRepository.findById(adoptionId)
                .orElseThrow(() -> new EntityNotFoundException("Усыновление не найдено"));
        adoption.setProbationStatus(Adoption.ProbationStatus.FAILED);
        adoptionRepository.save(adoption);
    }

    // Refactoring новый метод сброс пропущенных дней при отправке отчета
    @Transactional
    public void resetMissedDaysForUser(User user) {
        List<Adoption.ProbationStatus> activeStatuses = Arrays.asList(
                Adoption.ProbationStatus.IN_PROGRESS,
                Adoption.ProbationStatus.EXTENDED_14,
                Adoption.ProbationStatus.EXTENDED_30
        );
        adoptionRepository.findByUserAndProbationStatusIn(user, activeStatuses)
                .ifPresent(adoption -> {
                    adoption.setMissedDays(0);
                    adoptionRepository.save(adoption);
                });
    }
}
