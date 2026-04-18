package pro.sky.telegrambot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {
    private Long id;
    private Long userId;
    private LocalDate reportDate;
    private String photoUrl;
    private String diet;
    private String healthAndAdaptation;
    private String behaviorChanges;
    private boolean reviewed;
    private String volunteerFeedback;
}
