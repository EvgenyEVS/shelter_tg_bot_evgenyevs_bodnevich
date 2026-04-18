package pro.sky.telegrambot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pro.sky.telegrambot.dto.shelterDto.ShelterCreateDto;
import pro.sky.telegrambot.model.Shelter;

@Mapper(componentModel = "spring")
public interface ShelterMapper {

    Shelter toEntity(ShelterCreateDto createDto);

    @Mapping(target = "id", ignore = true)  // игнорируем id при обновлении
    void updateShelterFromDto(ShelterCreateDto dto, @MappingTarget Shelter shelter);
}