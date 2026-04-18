package pro.sky.telegrambot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import pro.sky.telegrambot.model.enums.PetType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Table (name = "shelter")
public class Shelter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "pet_type", nullable = false)
    private PetType petType;

    @Column(nullable = false, length = 255)
    private String address;

    private String shelterInfo;
    private String shelterSchedule;

    @Column(nullable = false, length = 255)
    private String routeSchemaUrl;

    @Column(nullable = false, length = 255)
    private String contacts;

    private String safetyPrecautionsAtShelter;

    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Pet> pets = new ArrayList<>();
}