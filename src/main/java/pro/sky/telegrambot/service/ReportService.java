package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.telegrambot.dto.ReportDto;
import pro.sky.telegrambot.model.Report;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.repository.ReportRepository;
import pro.sky.telegrambot.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final TelegramBot telegramBot;

    public void saveReport(Long chatId, String photoUrl, String diet, String health, String behavior) {
        User user = userRepository.findByChatId(chatId).orElseThrow();
        Report report = Report.builder()
                .user(user)
                .reportDate(LocalDate.now())
                .photoUrl(photoUrl)
                .diet(diet)
                .healthAndAdaptation(health)
                .behaviorChanges(behavior)
                .submittedAt(java.time.LocalDateTime.now())
                .reviewed(false)
                .build();
        reportRepository.save(report);
    }

    public boolean hasReportForDate(User user, LocalDate date) {
        return reportRepository.existsByUserAndReportDate(user, date);
    }

    @Transactional(readOnly = true)
    public List<ReportDto> getUnreviewedReports() {
        return reportRepository.findByReviewedFalse().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void markAsReviewed(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Отчет не найден: " + id));
        report.setReviewed(true);
        reportRepository.save(report);
    }

    @Transactional(readOnly = true)
    public ReportDto getReportDtoById(Long id) {
        return toDto(reportRepository.findById(id).orElseThrow());
    }

    @Transactional
    public void sendVolunteerFeedback(Long reportId, String feedback) {
        Report report = reportRepository.findById(reportId).orElseThrow();
        report.setVolunteerFeedback(feedback);
        reportRepository.save(report);

        String message = "Уважаемый усыновитель, мы заметили, что ты заполняешь отчет не так подробно, как необходимо. " +
                "Пожалуйста, подойди ответственнее к этому заданию. В противном случае волонтеры будут обязаны " +
                "самолично проверять условия содержания животного.\n\nКомментарий волонтера: " + feedback;
        telegramBot.execute(new SendMessage(report.getUser().getChatId(), message));
    }

    private ReportDto toDto(Report report) {
        return ReportDto.builder()
                .id(report.getId())
                .userId(report.getUser().getId())
                .reportDate(report.getReportDate())
                .photoUrl(report.getPhotoUrl())
                .diet(report.getDiet())
                .healthAndAdaptation(report.getHealthAndAdaptation())
                .behaviorChanges(report.getBehaviorChanges())
                .reviewed(report.isReviewed())
                .volunteerFeedback(report.getVolunteerFeedback())
                .build();
    }
}
