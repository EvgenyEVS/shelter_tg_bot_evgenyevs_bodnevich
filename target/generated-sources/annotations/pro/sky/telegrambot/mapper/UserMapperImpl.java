package pro.sky.telegrambot.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.dto.UserDto;
import pro.sky.telegrambot.model.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-25T01:22:29+0300",
    comments = "version: 1.6.0, compiler: javac, environment: Java 17.0.15 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setChatId( user.getChatId() );
        userDto.setTelegramUserName( user.getTelegramUserName() );
        userDto.setFirstName( user.getFirstName() );
        userDto.setLastName( user.getLastName() );
        userDto.setAge( user.getAge() );
        userDto.setPhoneNumber( user.getPhoneNumber() );
        userDto.setUserStatus( user.getUserStatus() );

        return userDto;
    }

    @Override
    public User toEntity(UserDto dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setChatId( dto.getChatId() );
        user.setTelegramUserName( dto.getTelegramUserName() );
        user.setFirstName( dto.getFirstName() );
        user.setLastName( dto.getLastName() );
        user.setAge( dto.getAge() );
        user.setPhoneNumber( dto.getPhoneNumber() );
        user.setUserStatus( dto.getUserStatus() );

        return user;
    }

    @Override
    public void updateFromDto(UserDto dto, User user) {
        if ( dto == null ) {
            return;
        }

        user.setChatId( dto.getChatId() );
        user.setTelegramUserName( dto.getTelegramUserName() );
        user.setFirstName( dto.getFirstName() );
        user.setLastName( dto.getLastName() );
        user.setAge( dto.getAge() );
        user.setPhoneNumber( dto.getPhoneNumber() );
        user.setUserStatus( dto.getUserStatus() );
    }
}
