package pro.sky.telegrambot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import pro.sky.telegrambot.dto.shelterDto.ShelterGeneralInfoDto;
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
@Table (name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PetType petType = PetType.UNKNOWN;

    private String petName;
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
    @JsonIgnore
    private Shelter shelter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    protected User owner;



    @JsonProperty("shelterId")
    public Long getShelterId() {
        return shelter != null ? shelter.getId() : null;
    }

    @JsonProperty("shelterAddress")
    public String getShelterAddress() {
        return shelter != null ? shelter.getAddress() : null;
    }

    @JsonProperty("shelterInfo")
    public ShelterGeneralInfoDto getShelterInfo() {
        if (shelter == null) return null;
        return new ShelterGeneralInfoDto(
                shelter.getShelterInfo(),
                shelter.getAddress()
        );
    }
}
