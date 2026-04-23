package pro.sky.telegrambot.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.dto.PetDto;
import pro.sky.telegrambot.model.Pet;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-23T21:30:14+0300",
    comments = "version: 1.6.0, compiler: javac, environment: Java 17.0.15 (Microsoft)"
)
@Component
public class PetMapperImpl implements PetMapper {

    @Override
    public void updatePetFromDto(PetDto dto, Pet pet) {
        if ( dto == null ) {
            return;
        }

        pet.setPetDescription( dto.pet_description() );
        pet.setId( dto.id() );
        pet.setPetType( dto.petType() );
        pet.setPetName( dto.petName() );
        pet.setBirthDay( dto.birthDay() );
        pet.setGender( dto.gender() );
        if ( dto.castratedOrSpayed() != null ) {
            pet.setCastratedOrSpayed( dto.castratedOrSpayed() );
        }
        pet.setPetStatus( dto.petStatus() );
        pet.setHealthInfo( dto.healthInfo() );
        pet.setSpecialNeeds( dto.specialNeeds() );
    }
}
