package pro.sky.telegrambot.model;

import com.pengrad.telegrambot.model.User;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

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

    private String shelterInfo;
    private String address;
    private String shelterSchedule; // расписание
    private String routeSchemaUrl; // схема проезда в картинке. Хранится удаленно, получаем по URL
    private String contacts;
    private String safetyPrecautionsAtShelter;


    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pet> pets = new ArrayList<>();

}
