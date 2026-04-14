package pro.sky.telegrambot.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "adoptions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Adoption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    private LocalDate startDate;

    @Enumerated(EnumType.STRING)
    private ProbationStatus probationStatus = ProbationStatus.IN_PROGRESS;

    private LocalDate probationEndDate;

    public enum ProbationStatus {
        IN_PROGRESS, EXTENDED_14, EXTENDED_30, PASSED, FAILED
    }
}
