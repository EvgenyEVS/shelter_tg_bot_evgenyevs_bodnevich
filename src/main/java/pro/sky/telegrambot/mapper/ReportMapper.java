package pro.sky.telegrambot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pro.sky.telegrambot.dto.ReportDto;
import pro.sky.telegrambot.model.Report;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    @Mapping(source = "user.id", target = "userId")
    ReportDto toDto(Report report);

    @Mapping(target = "user", ignore = true)
    Report toEntity(ReportDto dto);
}
