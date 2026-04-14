package pro.sky.telegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.dto.ReportDto;
import pro.sky.telegrambot.service.ReportService;

import java.util.List;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
@Tag(name = "Отчеты усыновителей", description = "REST API для работы с отчетами")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/unreviewed")
    @Operation(summary = "Получить список необработанных отчетов")
    public ResponseEntity<List<ReportDto>> getUnreviewedReports() {
        return ResponseEntity.ok(reportService.getUnreviewedReports());
    }

    @PostMapping("/{id}/review")
    @Operation(summary = "Отметить отчет как просмотренный")
    public ResponseEntity<Void> markAsReviewed(@PathVariable Long id) {
        reportService.markAsReviewed(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/export")
    @Operation(summary = "Выгрузить отчет по идентификатору")
    public ResponseEntity<ReportDto> exportReports(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.getReportDtoById(id));
    }

    @PostMapping("/{id}/feedback")
    @Operation(summary = "Отправить сообщение о ненадлежащем отчете")
    public ResponseEntity<Void> sendFeedback(@PathVariable Long id, @RequestParam String feedback) {
        reportService.sendVolunteerFeedback(id, feedback);
        return ResponseEntity.ok().build();
    }
}
