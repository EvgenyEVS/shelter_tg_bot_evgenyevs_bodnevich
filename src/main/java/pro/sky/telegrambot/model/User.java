package pro.sky.telegrambot.model;

import lombok.*;
import pro.sky.telegrambot.model.enums.PetType;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(exclude = "pets")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id", unique = true, nullable = false)
    private Long chatId;

    @Column(name = "telegram_user_name")
    private String telegramUserName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private Integer age;

    @Pattern(regexp = "^\\+7-9\\d{2}-\\d{3}-\\d{2}-\\d{2}$", message = "Номер телефона должен быть в формате +7-9XX-XXX-XX-XX")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private PetType selectedShelterType = PetType.UNKNOWN;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Pet> pets = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus = UserStatus.NON_ACTIVE;

    private boolean volunteer = false;

    @Enumerated(EnumType.STRING)
    private UserDialogState dialogState = UserDialogState.START;

    private LocalDateTime adoptionalStartDate;

    public enum UserStatus {
        ORDINARY, ADOPTER, GRADUATED, NON_ACTIVE
    }

    public enum UserDialogState {
        START,
        SHELTER_INFO,
        ADOPTION_ADVICE,
        REPORT,
        WAITING_VOLUNTEER,
        WAITING_PHONE,
        WAITING_REPORT_PHOTO,
        WAITING_REPORT_DIET,
        WAITING_REPORT_HEALTH,
        WAITING_REPORT_BEHAVIOR
    }
}
