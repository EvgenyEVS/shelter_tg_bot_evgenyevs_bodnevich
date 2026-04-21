package pro.sky.telegrambot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pro.sky.telegrambot.dto.shelterDto.ShelterCreateDto;
import pro.sky.telegrambot.model.Shelter;

@Mapper(componentModel = "spring")
public interface ShelterMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pets", ignore = true)
    Shelter toEntity(ShelterCreateDto createDto);

    @Mapping(target = "id", ignore = true) // игнорируем id при обновлении
    @Mapping(target = "pets", ignore = true)
    void updateShelterFromDto(ShelterCreateDto dto, @MappingTarget Shelter shelter);
}