package pro.sky.telegrambot.unit.model;

import org.junit.jupiter.api.Test;
import pro.sky.telegrambot.model.Report;
import pro.sky.telegrambot.model.User;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ReportTest {

    @Test
    void builder_shouldCreateReportWithDefaults() {
        User user = new User();
        LocalDate reportDate = LocalDate.now();

        Report report = Report.builder()
                .user(user)
                .reportDate(reportDate)
                .photoUrl("photo.jpg")
                .diet("Meat")
                .healthAndAdaptation("Good")
                .behaviorChanges("Active")
                .build();

        assertThat(report.getUser()).isEqualTo(user);
        assertThat(report.getReportDate()).isEqualTo(reportDate);
        assertThat(report.getPhotoUrl()).isEqualTo("photo.jpg");
        assertThat(report.getDiet()).isEqualTo("Meat");
        assertThat(report.getHealthAndAdaptation()).isEqualTo("Good");
        assertThat(report.getBehaviorChanges()).isEqualTo("Active");
        assertThat(report.getSubmittedAt()).isNotNull();
        assertThat(report.isReviewed()).isFalse();
        assertThat(report.getVolunteerFeedback()).isNull();
    }

    @Test
    void settersAndGetters_shouldWork() {
        Report report = new Report();
        report.setId(10L);
        report.setReviewed(true);
        report.setVolunteerFeedback("Excellent");

        assertThat(report.getId()).isEqualTo(10L);
        assertThat(report.isReviewed()).isTrue();
        assertThat(report.getVolunteerFeedback()).isEqualTo("Excellent");
    }
}
