package pro.sky.telegrambot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pro.sky.telegrambot.dto.UserDto;
import pro.sky.telegrambot.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(UserDto dto);

    @Mapping(target = "id", ignore = true)
    void updateFromDto(UserDto dto, @MappingTarget User user);
}
