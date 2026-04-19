package pro.sky.telegrambot.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.dto.shelterDto.ShelterCreateDto;
import pro.sky.telegrambot.model.Shelter;
import pro.sky.telegrambot.model.enums.PetType;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T20:09:40+0300",
    comments = "version: 1.6.0, compiler: javac, environment: Java 17.0.15 (Microsoft)"
)
@Component
public class ShelterMapperImpl implements ShelterMapper {

    @Override
    public Shelter toEntity(ShelterCreateDto createDto) {
        if ( createDto == null ) {
            return null;
        }

        Shelter shelter = new Shelter();

        if ( createDto.petType() != null ) {
            shelter.setPetType( Enum.valueOf( PetType.class, createDto.petType() ) );
        }
        shelter.setAddress( createDto.address() );
        shelter.setShelterInfo( createDto.shelterInfo() );
        shelter.setShelterSchedule( createDto.shelterSchedule() );
        shelter.setRouteSchemaUrl( createDto.routeSchemaUrl() );
        shelter.setContacts( createDto.contacts() );
        shelter.setSafetyPrecautionsAtShelter( createDto.safetyPrecautionsAtShelter() );

        return shelter;
    }

    @Override
    public void updateShelterFromDto(ShelterCreateDto dto, Shelter shelter) {
        if ( dto == null ) {
            return;
        }

        if ( dto.petType() != null ) {
            shelter.setPetType( Enum.valueOf( PetType.class, dto.petType() ) );
        }
        else {
            shelter.setPetType( null );
        }
        shelter.setAddress( dto.address() );
        shelter.setShelterInfo( dto.shelterInfo() );
        shelter.setShelterSchedule( dto.shelterSchedule() );
        shelter.setRouteSchemaUrl( dto.routeSchemaUrl() );
        shelter.setContacts( dto.contacts() );
        shelter.setSafetyPrecautionsAtShelter( dto.safetyPrecautionsAtShelter() );
    }
}
