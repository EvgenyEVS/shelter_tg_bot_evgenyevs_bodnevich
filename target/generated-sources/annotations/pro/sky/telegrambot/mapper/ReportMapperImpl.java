package pro.sky.telegrambot.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.dto.ReportDto;
import pro.sky.telegrambot.model.Report;
import pro.sky.telegrambot.model.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-29T23:53:07+0300",
    comments = "version: 1.6.0, compiler: javac, environment: Java 17.0.15 (Microsoft)"
)
@Component
public class ReportMapperImpl implements ReportMapper {

    @Override
    public ReportDto toDto(Report report) {
        if ( report == null ) {
            return null;
        }

        ReportDto.ReportDtoBuilder reportDto = ReportDto.builder();

        reportDto.userId( reportUserId( report ) );
        reportDto.id( report.getId() );
        reportDto.reportDate( report.getReportDate() );
        reportDto.photoUrl( report.getPhotoUrl() );
        reportDto.diet( report.getDiet() );
        reportDto.healthAndAdaptation( report.getHealthAndAdaptation() );
        reportDto.behaviorChanges( report.getBehaviorChanges() );
        reportDto.reviewed( report.isReviewed() );
        reportDto.volunteerFeedback( report.getVolunteerFeedback() );

        return reportDto.build();
    }

    @Override
    public Report toEntity(ReportDto dto) {
        if ( dto == null ) {
            return null;
        }

        Report.ReportBuilder report = Report.builder();

        report.id( dto.getId() );
        report.reportDate( dto.getReportDate() );
        report.photoUrl( dto.getPhotoUrl() );
        report.diet( dto.getDiet() );
        report.healthAndAdaptation( dto.getHealthAndAdaptation() );
        report.behaviorChanges( dto.getBehaviorChanges() );
        report.reviewed( dto.isReviewed() );
        report.volunteerFeedback( dto.getVolunteerFeedback() );

        return report.build();
    }

    private Long reportUserId(Report report) {
        User user = report.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getId();
    }
}
