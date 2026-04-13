package pro.sky.telegrambot.mapper;

import org.mapstruct.Mapper;
import pro.sky.telegrambot.dto.shelterDto.ShelterCreateDto;
import pro.sky.telegrambot.model.Shelter;

@Mapper
public interface ShelterMapper {
    Shelter toEntity(ShelterCreateDto createDto);
}
