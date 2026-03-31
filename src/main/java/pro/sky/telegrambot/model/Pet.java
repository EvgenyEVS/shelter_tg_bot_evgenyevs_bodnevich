package pro.sky.telegrambot.model;

import lombok.*;
import pro.sky.telegrambot.model.enums.Gender;
import pro.sky.telegrambot.model.enums.PetStatus;
import pro.sky.telegrambot.model.enums.PetType;

import javax.persistence.*;
import java.time.LocalDate;

@Entity

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString
@Inheritance

public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected PetType petType = PetType.UNKNOWN;

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


}
