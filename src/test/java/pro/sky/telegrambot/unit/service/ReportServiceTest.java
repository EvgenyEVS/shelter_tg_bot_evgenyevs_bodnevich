package pro.sky.telegrambot.unit.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.model.Report;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.repository.ReportRepository;
import pro.sky.telegrambot.repository.UserRepository;
import pro.sky.telegrambot.service.ReportService;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private ReportRepository reportRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TelegramBot telegramBot;

    @InjectMocks
    private ReportService reportService;

    @Test
    void saveReport_shouldCreateAndSave() {
        Long chatId = 123L;
        User user = new User();
        when(userRepository.findByChatId(chatId)).thenReturn(Optional.of(user));
        when(reportRepository.save(any(Report.class))).thenAnswer(invocation -> invocation.getArgument(0));

        reportService.saveReport(chatId, "photoUrl", "diet", "health", "behavior");

        verify(reportRepository).save(argThat(r ->
                r.getUser() == user &&
                        r.getPhotoUrl().equals("photoUrl") &&
                        r.getDiet().equals("diet") &&
                        r.getHealthAndAdaptation().equals("health") &&
                        r.getBehaviorChanges().equals("behavior") &&
                        !r.isReviewed()
        ));
    }

    @Test
    void sendVolunteerFeedback_shouldSaveAndSendMessage() {
        Long reportId = 1L;
        User user = new User();
        user.setChatId(123L);
        Report report = Report.builder().id(reportId).user(user).build();
        when(reportRepository.findById(reportId)).thenReturn(Optional.of(report));
        when(reportRepository.save(any(Report.class))).thenReturn(report);

        reportService.sendVolunteerFeedback(reportId, "Bad report");

        assertThat(report.getVolunteerFeedback()).isEqualTo("Bad report");
        verify(telegramBot).execute(any(SendMessage.class));
    }

    @Test
    void markAsReviewed_notFound_throwsException() {
        when(reportRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> reportService.markAsReviewed(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
