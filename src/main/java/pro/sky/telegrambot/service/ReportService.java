package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.telegrambot.dto.ReportDto;
import pro.sky.telegrambot.model.Report;
import pro.sky.telegrambot.repository.ReportRepository;
import pro.sky.telegrambot.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final TelegramBot telegramBot;

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
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Отчет не найден: " + id));
        return toDto(report);
    }

    @Transactional
    public void sendVolunteerFeedback(Long reportId, String feedback) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("Отчет не найден: " + reportId));
        report.setVolunteerFeedback(feedback);
        reportRepository.save(report);

        String message = "Уважаемый усыновитель, мы заметили, что ты заполняешь отчет не так подробно, как необходимо. " +
                "Пожалуйста, подойди ответственнее к этому заданию. В противном случае волонтеры будут обязаны " +
                "самолично проверять условия содержания животного.\n\nКомментарий волонтера: " + feedback;
        SendMessage sendMessage = new SendMessage(report.getUser().getChatId(), message);
        telegramBot.execute(sendMessage);
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
                .volinteerFeedback(report.getVolunteerFeedback())
                .build()
    }
}
