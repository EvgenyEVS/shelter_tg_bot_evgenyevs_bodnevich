package pro.sky.telegrambot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@Getter
@Setter


public class Cat extends Pet {

    public Cat() {
        super();
    }

    public Cat(String name,
               LocalDate birthDay,
               Gender gender
           ) {
        super(name, birthDay, gender);
    }

    public Cat(String name,
               LocalDate birthDay,
               Gender gender,
               boolean castratedOrSpayed,
               PetStatus petStatus,
               String pet_description,
               String healthInfo,
               String specialNeeds) {
        super(name,
                birthDay,
                gender,
                castratedOrSpayed,
                petStatus,
                pet_description,
                healthInfo,
                specialNeeds);
    }


}
