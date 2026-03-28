package pro.sky.telegrambot.model;

import lombok.*;

import javax.persistence.*;

@Entity

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString

public abstract class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String name;
    protected Integer age;
    protected Gender gender = Gender.UNKNOWN;
    protected boolean castratedOrSpayed;

    @ManyToOne
    protected User owner;



    protected enum Gender {
        MALE,
        FEMALE,
        UNKNOWN
    }


}
