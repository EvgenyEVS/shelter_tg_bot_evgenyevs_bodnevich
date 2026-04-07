package pro.sky.telegrambot.model;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
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
    private Long id;
    private PetType petType = PetType.UNKNOWN;

    private String name;
    private LocalDate birthDay;
    private Gender gender = Gender.UNKNOWN;
    private boolean castratedOrSpayed;
    private PetStatus petStatus = PetStatus.NOT_AVAILABLE;
    private String pet_description;
    private String healthInfo;
    private String specialNeeds;


    //@ManyToOne
    //protected User owner;


}
