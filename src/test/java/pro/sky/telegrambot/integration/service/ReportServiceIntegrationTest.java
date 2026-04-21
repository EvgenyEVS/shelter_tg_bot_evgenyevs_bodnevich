package pro.sky.telegrambot.integration.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.telegrambot.dto.ReportDto;
import pro.sky.telegrambot.model.Report;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.repository.ReportRepository;
import pro.sky.telegrambot.repository.UserRepository;
import pro.sky.telegrambot.service.ReportService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ReportServiceIntegrationTest {

    @Autowired
    private ReportService reportService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReportRepository reportRepository;

    @MockBean
    private TelegramBot telegramBot;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setChatId(12345L);
        testUser.setFirstName("ReportTest");
        testUser.setUserStatus(User.UserStatus.ADOPTER);
        testUser = userRepository.save(testUser);
    }

    @Test
    void saveReport_shouldCreateAndSaveReport() {
        reportService.saveReport(testUser.getChatId(), "photoFileId", "diet content", "health content", "behavior content");

        List<Report> reports = reportRepository.findByUserAndReportDate(testUser, LocalDate.now());
        assertThat(reports).hasSize(1);
        Report saved = reports.get(0);
        assertThat(saved.getPhotoUrl()).isEqualTo("photoFileId");
        assertThat(saved.getDiet()).isEqualTo("diet content");
        assertThat(saved.getHealthAndAdaptation()).isEqualTo("health content");
        assertThat(saved.getBehaviorChanges()).isEqualTo("behavior content");
        assertThat(saved.isReviewed()).isFalse();
        assertThat(saved.getVolunteerFeedback()).isNull();
    }

    @Test
    void hasReportForDate_shouldReturnTrueIfExists() {
        Report report = Report.builder()
                .user(testUser)
                .reportDate(LocalDate.now())
                .diet("test")
                .build();
        reportRepository.save(report);

        boolean exists = reportService.hasReportForDate(testUser, LocalDate.now());
        boolean notExists = reportService.hasReportForDate(testUser, LocalDate.now().minusDays(1));

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    void getUnreviewedReports_shouldReturnOnlyUnreviewed() {
        Report reviewed = Report.builder()
                .user(testUser).reviewed(true).diet("reviewed").build();
        Report unreviewed1 = Report.builder()
                .user(testUser).reviewed(false).diet("unreviewed1").build();
        Report unreviewed2 = Report.builder()
                .user(testUser).reviewed(false).diet("unreviewed2").build();
        reportRepository.save(reviewed);
        reportRepository.save(unreviewed1);
        reportRepository.save(unreviewed2);

        List<ReportDto> unreviewed = reportService.getUnreviewedReports();

        assertThat(unreviewed).hasSize(2);
        assertThat(unreviewed).extracting(ReportDto::getDiet)
                .containsExactlyInAnyOrder("unreviewed1", "unreviewed2");
        assertThat(unreviewed).allMatch(dto -> !dto.isReviewed());
    }

    @Test
    void markAsReviewed_shouldUpdateFlag() {
        Report report = Report.builder()
                .user(testUser).reviewed(false).build();
        report = reportRepository.save(report);

        reportService.markAsReviewed(report.getId());

        Report updated = reportRepository.findById(report.getId()).orElseThrow();
        assertThat(updated.isReviewed()).isTrue();
    }

    @Test
    void markAsReviewed_notFound_throwsException() {
        assertThatThrownBy(() -> reportService.markAsReviewed(999L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void getReportDtoById_shouldReturnCorrectDto() {
        Report report = Report.builder()
                .user(testUser)
                .diet("special diet")
                .reviewed(false)
                .build();
        report = reportRepository.save(report);

        ReportDto dto = reportService.getReportDtoById(report.getId());

        assertThat(dto.getId()).isEqualTo(report.getId());
        assertThat(dto.getDiet()).isEqualTo("special diet");
        assertThat(dto.isReviewed()).isFalse();
        assertThat(dto.getUserId()).isEqualTo(testUser.getId());
    }

    @Test
    void sendVolunteerFeedback_shouldSaveFeedbackAndSendMessage() {
        Report report = Report.builder()
                .user(testUser)
                .reviewed(false)
                .build();
        report = reportRepository.save(report);
        String feedback = "Please provide more details about diet";

        reportService.sendVolunteerFeedback(report.getId(), feedback);

        Report updated = reportRepository.findById(report.getId()).orElseThrow();
        assertThat(updated.getVolunteerFeedback()).isEqualTo(feedback);
        verify(telegramBot).execute(any(SendMessage.class));
    }

    @Test
    void sendVolunteerFeedback_reportNotFound_throwsException() {
        assertThatThrownBy(() -> reportService.sendVolunteerFeedback(999L, "feedback"))
                .isInstanceOf(EntityNotFoundException.class);
    }
}