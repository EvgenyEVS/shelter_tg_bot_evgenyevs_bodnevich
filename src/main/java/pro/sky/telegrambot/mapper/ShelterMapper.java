package pro.sky.telegrambot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import pro.sky.telegrambot.dto.shelterDto.ShelterCreateDto;
import pro.sky.telegrambot.dto.shelterDto.ShelterResponseDto;
import pro.sky.telegrambot.model.Shelter;
import pro.sky.telegrambot.model.enums.PetType;

@Mapper(componentModel = "spring")
public interface ShelterMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pets", ignore = true)
    Shelter toEntity(ShelterCreateDto createDto);

    @Mapping(target = "id", ignore = true) // игнорируем id при обновлении
    @Mapping(target = "pets", ignore = true)
    void updateShelterFromDto(ShelterCreateDto dto, @MappingTarget Shelter shelter);

    @Named("stringToPetType")
    default PetType stringToPetType(String petType) {
        if (petType == null) return PetType.UNKNOWN;
        try {
            return PetType.valueOf(petType.toUpperCase());
        } catch (IllegalArgumentException e) {
            return PetType.UNKNOWN;
        }
    }

    @Mapping(target = "petType", source = "petType")
    ShelterResponseDto toResponseDto(Shelter shelter);

}