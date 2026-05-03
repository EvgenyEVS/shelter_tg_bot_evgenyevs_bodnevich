package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.telegrambot.model.Adoption;
import pro.sky.telegrambot.model.User;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReportReminderScheduler {
    private final AdoptionService adoptionService;
    private final ReportService reportService;
    private final VolunteerNotificationService volunteerService;
    private final TelegramBot telegramBot;

    @Scheduled(cron = "0 0 21 * * *")
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void remindMissingReports() {
        List<Adoption> active = adoptionService.getActiveAdoptions();
        for (Adoption adoption : active) {
            User user = adoption.getUser();
            boolean hasReportToday = reportService.hasReportForDate(user, LocalDate.now());
            if (!hasReportToday) {
                telegramBot.execute(new SendMessage(user.getChatId(), "📝 Пожалуйста, пришлите ежедневный отчёт о питомце."));
                int missed = adoption.getMissedDays() + 1;
                adoption.setMissedDays(missed);
                adoptionService.save(adoption);
                if (missed >= 2) {
                    volunteerService.notifyAboutMissedReports(user);
                }
            }
        }
    }

    @Scheduled(cron = "0 0 8 * * *")
    @Transactional(readOnly = true)
    public void checkProbationPeriods() {
        List<Adoption> ending = adoptionService.findByProbationEndDateBefore(LocalDate.now());
        for (Adoption adoption : ending) {
            volunteerService.notifyForDecision(adoption);
        }
    }
}