package pro.sky.telegrambot.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate reportDate;

    private String photoUrl;
    private String diet;
    private String healthAndAdaptation;
    private String behaviorChanges;

    @Column(nullable = false)
    private LocalDateTime submittedAt = LocalDateTime.now();

    private boolean reviewed = false;

    private String volunteerFeedback;
}
