package pro.sky.telegrambot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import pro.sky.telegrambot.dto.PetDto;
import pro.sky.telegrambot.model.Pet;

@Mapper(componentModel = "spring")
public interface PetMapper {
    void updatePetFromDto(PetDto dto, @MappingTarget Pet pet);
}
