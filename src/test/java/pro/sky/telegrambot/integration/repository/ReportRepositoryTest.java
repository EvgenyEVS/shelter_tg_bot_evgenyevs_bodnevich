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
import pro.sky.telegrambot.model.Report;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.repository.ReportRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
//@ContextConfiguration(classes = TestJpaConfig.class)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReportRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReportRepository reportRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setChatId(123L);
        user = entityManager.persistAndFlush(user);
    }

    @Test
    void findByUserAndReportDate_shouldReturnReports() {
        LocalDate today = LocalDate.now();
        Report report1 = Report.builder().user(user).reportDate(today).diet("Meat").build();
        Report report2 = Report.builder().user(user).reportDate(today.minusDays(1)).build();
        entityManager.persist(report1);
        entityManager.persist(report2);
        entityManager.flush();

        List<Report> result = reportRepository.findByUserAndReportDate(user, today);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDiet()).isEqualTo("Meat");
    }

    @Test
    void findByReviewedFalse_shouldReturnUnreviewed() {
        Report reviewed = Report.builder()
                .user(user)
                .reviewed(true)
                .reportDate(LocalDate.now())
                .submittedAt(LocalDate.now().atStartOfDay())
                .diet("test")
                .build();
        Report unreviewed1 = Report.builder()
                .user(user)
                .reviewed(false)
                .reportDate(LocalDate.now())
                .submittedAt(LocalDate.now().atStartOfDay())
                .diet("test1")
                .build();
        Report unreviewed2 = Report.builder()
                .user(user)
                .reviewed(false)
                .reportDate(LocalDate.now())
                .submittedAt(LocalDate.now().atStartOfDay())
                .diet("test2")
                .build();
//        Report reviewed = Report.builder().user(user).reviewed(true).build();
//        Report unreviewed1 = Report.builder().user(user).reviewed(false).build();
//        Report unreviewed2 = Report.builder().user(user).reviewed(false).build();
        entityManager.persist(reviewed);
        entityManager.persist(unreviewed1);
        entityManager.persist(unreviewed2);
        entityManager.flush();

        List<Report> result = reportRepository.findByReviewedFalse();
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(r -> !r.isReviewed());
    }

    @Test
    void findByUserOrderByReportDateDesc_shouldReturnSorted() {
        Report report1 = Report.builder().user(user).reportDate(LocalDate.of(2024, 1, 1)).build();
        Report report2 = Report.builder().user(user).reportDate(LocalDate.of(2024, 3, 1)).build();
        Report report3 = Report.builder().user(user).reportDate(LocalDate.of(2024, 2, 1)).build();
        entityManager.persist(report1);
        entityManager.persist(report2);
        entityManager.persist(report3);
        entityManager.flush();

        List<Report> result = reportRepository.findByUserOrderByReportDateDesc(user);
        assertThat(result).hasSize(3);
        assertThat(result).extracting(Report::getReportDate)
                .containsExactly(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 2, 1), LocalDate.of(2024, 1, 1));
    }

    @Test
    void existsByUserAndReportDate_shouldReturnTrueIfExists() {
        LocalDate date = LocalDate.now();
        Report report = Report.builder().user(user).reportDate(date).build();
        entityManager.persistAndFlush(report);

        boolean exists = reportRepository.existsByUserAndReportDate(user, date);
        assertThat(exists).isTrue();

        boolean notExists = reportRepository.existsByUserAndReportDate(user, date.minusDays(10));
        assertThat(notExists).isFalse();
    }
}