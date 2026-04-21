package pro.sky.telegrambot.unit.model;

import org.junit.jupiter.api.Test;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.model.enums.PetType;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserTest {

    @Test
    void setterAndGetters_shouldWork() {
        User user = new User();
        user.setId(1L);
        user.setChatId(123456L);
        user.setTelegramUserName("@test_user");
        user.setFirstName("Nikita");
        user.setLastName("Ivanov");
        user.setAge(24);
        user.setPhoneNumber("+7-999-123-44-55");
        user.setSelectedShelterType(PetType.CAT);
        user.setUserStatus(User.UserStatus.ADOPTER);
        user.setVolunteer(true);
        user.setDialogState(User.UserDialogState.REPORT);
        user.setAdoptionalStartDate(java.time.LocalDateTime.now());

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getChatId()).isEqualTo(123456L);
        assertThat(user.getTelegramUserName()).isEqualTo("@test_user");
        assertThat(user.getFirstName()).isEqualTo("Nikita");
        assertThat(user.getLastName()).isEqualTo("Ivanov");
        assertThat(user.getAge()).isEqualTo(24);
        assertThat(user.getPhoneNumber()).isEqualTo("+7-999-123-44-55");
        assertThat(user.getSelectedShelterType()).isEqualTo(PetType.CAT);
        assertThat(user.getUserStatus()).isEqualTo(User.UserStatus.ADOPTER);
        assertThat(user.isVolunteer()).isTrue();
        assertThat(user.getDialogState()).isEqualTo(User.UserDialogState.REPORT);
        assertThat(user.getAdoptionalStartDate()).isNotNull();
    }
}
