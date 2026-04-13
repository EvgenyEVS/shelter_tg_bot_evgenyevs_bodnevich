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

public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PetType petType = PetType.UNKNOWN;

    private String name;
    private LocalDate birthDay;

    @Enumerated(EnumType.STRING)
    private Gender gender = Gender.UNKNOWN;

    private boolean castratedOrSpayed;

    @Enumerated(EnumType.STRING)
    private PetStatus petStatus = PetStatus.NOT_AVAILABLE;

    private String petDescription;
    private String healthInfo;
    private String specialNeeds;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    protected User owner;
}
