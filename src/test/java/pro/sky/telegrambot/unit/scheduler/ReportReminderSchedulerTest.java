package pro.sky.telegrambot.unit.scheduler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.model.Adoption;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.service.AdoptionService;
import pro.sky.telegrambot.service.ReportReminderScheduler;
import pro.sky.telegrambot.service.ReportService;
import pro.sky.telegrambot.service.VolunteerNotificationService;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportReminderSchedulerTest {

    @Mock
    private AdoptionService adoptionService;
    @Mock
    private ReportService reportService;
    @Mock
    private VolunteerNotificationService volunteerNotificationService;
    @Mock
    private TelegramBot telegramBot;

    @InjectMocks
    private ReportReminderScheduler scheduler;

    @Test
    void remindMissingReport_shouldSendReminderAndNotifyVolunteerAfterTwoMisses() {
        User user = new User();
        user.setChatId(123L);
        Adoption adoption = Adoption.builder().user(user).missedDays(1).build();
        when(adoptionService.getActiveAdoptions()).thenReturn(List.of(adoption));
        when(reportService.hasReportForDate(any(), any(LocalDate.class))).thenReturn(false);

        scheduler.remindMissingReports();

        verify(telegramBot).execute(any(SendMessage.class));
        assertThat(adoption.getMissedDays()).isEqualTo(2);
        verify(volunteerNotificationService).notifyAboutMissedReports(user);
        verify(adoptionService).save(adoption);
    }

    @Test
    void checkProbationPeriods_shouldNotifyVolunteer() {
        Adoption adoption = Adoption.builder().id(1L).build();
        when(adoptionService.findByProbationEndDateBefore(any(LocalDate.class))).thenReturn(List.of(adoption));

        scheduler.checkProbationPeriods();

        verify(volunteerNotificationService).notifyForDecision(adoption);
    }
}
