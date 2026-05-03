package pro.sky.telegrambot.mapper;

import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.dto.PetDto;
import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.model.enums.Gender;
import pro.sky.telegrambot.model.enums.PetStatus;
import pro.sky.telegrambot.model.enums.PetType;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-03T23:22:30+0300",
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

    @Override
    public PetDto toDto(Pet pet) {
        if ( pet == null ) {
            return null;
        }

        String pet_description = null;
        Long id = null;
        PetType petType = null;
        String petName = null;
        LocalDate birthDay = null;
        Gender gender = null;
        Boolean castratedOrSpayed = null;
        PetStatus petStatus = null;
        String healthInfo = null;
        String specialNeeds = null;

        pet_description = pet.getPetDescription();
        id = pet.getId();
        petType = pet.getPetType();
        petName = pet.getPetName();
        birthDay = pet.getBirthDay();
        gender = pet.getGender();
        castratedOrSpayed = pet.isCastratedOrSpayed();
        petStatus = pet.getPetStatus();
        healthInfo = pet.getHealthInfo();
        specialNeeds = pet.getSpecialNeeds();

        PetDto petDto = new PetDto( id, petType, petName, birthDay, gender, castratedOrSpayed, petStatus, pet_description, healthInfo, specialNeeds );

        return petDto;
    }
}
