package pro.sky.telegrambot.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "chatId")
@ToString

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;

    private String telegramUserName;
    private String firstName;
    private String lastName;
    private Integer age;
    private String phoneNumber;

    //@OneToMany()
    private Set<Pet> pets = new HashSet<>();

    //@OneToMany()
    private List<Report> userReports = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ShelterType shelterType = ShelterType.SHELTER_UNKNOWN;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus = UserStatus.NON_ACTIVE;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "assigned_volunteer_id")
//    private Volunteer assignedVolunteer;


    public enum UserStatus {
        ORDINARY,
        ADOPTER,
        GRADUATED,
        NON_ACTIVE
    }




}
