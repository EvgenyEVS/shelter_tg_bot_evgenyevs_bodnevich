package pro.sky.telegrambot.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pro.sky.telegrambot.model.enums.PetType;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdoptionInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PetType petType;

    private String adviceBefore;
    private String documentSet;
    private String adviceTransport;
    private String adviceHomeForChild;
    private String adviceHomeForAdult;
    private String adviceHomeForDisabilities;
    private String refusalSet;

// - Бот может принять и записать контактные данные для связи.
// - Если бот не может ответить на вопросы клиента, то можно позвать волонтера.

}
