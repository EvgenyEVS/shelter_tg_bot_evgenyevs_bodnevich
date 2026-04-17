package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.Report;
import pro.sky.telegrambot.model.User;

import java.time.LocalDate;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByUserAndReportDate(User user, LocalDate date);
    List<Report> findByReviewedFalse();
    List<Report> findByUserOrderByReportDateDesc(User user);
    boolean existsByUserAndReportDate(User user, LocalDate date);
}
