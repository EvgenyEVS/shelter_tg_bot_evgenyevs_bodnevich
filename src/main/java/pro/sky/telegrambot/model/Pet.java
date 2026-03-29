package pro.sky.telegrambot.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString
@MappedSuperclass

public abstract class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String name;
    protected LocalDate birthDay;
    protected Gender gender = Gender.UNKNOWN;
    protected boolean castratedOrSpayed;
    protected ShelterType shelterType = ShelterType.SHELTER_UNKNOWN;
    protected PetStatus petStatus = PetStatus.NOT_AVAILABLE;
    protected String pet_description;
    protected String healthInfo;
    protected String specialNeeds;

    @ManyToOne
    protected User owner;

    public Pet(String name,
               LocalDate birthDay,
               Gender gender) {
    }

    public Pet(String name,
               LocalDate birthDay,
               Gender gender,
               boolean castratedOrSpayed,
               PetStatus petStatus,
               String pet_description,
               String healthInfo,
               String specialNeeds) {
        this.name = name;
        this.birthDay = birthDay;
        this.gender = gender;
        this.castratedOrSpayed = castratedOrSpayed;
        this.petStatus = petStatus;
        this.pet_description = pet_description;
        this.healthInfo = healthInfo;
        this.specialNeeds = specialNeeds;
    }

    // связь с приютом организовать в наследниках, т.к. БД разные



    public enum Gender {
        MALE,
        FEMALE,
        UNKNOWN
    }

    protected enum PetStatus {
        AVAILABLE,
        ON_TRIAL,
        ADOPTED,
        NOT_AVAILABLE
    }

    //auto initialization shelterType
//    protected abstract ShelterType getShelterType();
//    {
//        this.shelterType = getShelterType();
//    }

}
