package pro.sky.telegrambot.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pengrad.telegrambot.model.User;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import pro.sky.telegrambot.model.enums.PetType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString

public class Shelter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private PetType petType;
    private String shelterInfo;
    private String address;
    private String shelterSchedule; // расписание
    private String routeSchemaUrl; // Схема проезда в картинке. Хранится удаленно, получаем по URL
    private String contacts;
    private String safetyPrecautionsAtShelter;

    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Pet> pets = new ArrayList<>();


    // Helper methods
    public void addPet(Pet pet) {
        pets.add(pet);
        pet.setShelter(this);
    }

    public void removePet(Pet pet) {
        pets.remove(pet);
        pet.setShelter(null);
    }

}
