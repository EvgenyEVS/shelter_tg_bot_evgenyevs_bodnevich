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

public abstract class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String name;
    protected LocalDate birthDay;
    protected Gender gender = Gender.UNKNOWN;
    protected boolean castratedOrSpayed;
    protected PetStatus petStatus = PetStatus.NOT_AVAILABLE;
    protected String pet_description;
    protected String healthInfo;
    protected String specialNeeds;

    @ManyToOne
    protected User owner;



    // связь с приютом организовать в наследниках, т.к. БД разные



    protected enum Gender {
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


}
