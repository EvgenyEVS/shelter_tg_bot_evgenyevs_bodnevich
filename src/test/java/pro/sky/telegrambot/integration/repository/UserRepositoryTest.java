package pro.sky.telegrambot.integration.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.repository.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByChatId_shouldReturnUser() {
        User user = new User();
        user.setChatId(123456L);
        user.setFirstName("Musya");
        entityManager.persistAndFlush(user);

        var found = userRepository.findByChatId(123456L);
        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("Musya");
    }

    @Test
    void findByChatId_shouldReturnEmptyWhenNotFound() {
        assertThat(userRepository.findByChatId(999L)).isEmpty();
    }

    @Test
    void findByVolunteerTrue_shouldReturnOnlyVolunteers() {
        User volunteer1 = new User();
        volunteer1.setVolunteer(true);
        volunteer1.setChatId(1L);
        User volunteer2 = new User();
        volunteer2.setVolunteer(true);
        volunteer2.setChatId(2L);
        User nonVolunteer =  new User();
        nonVolunteer.setVolunteer(false);
        nonVolunteer.setChatId(3L);
        entityManager.persist(volunteer1);
        entityManager.persist(volunteer2);
        entityManager.persist(nonVolunteer);
        entityManager.flush();

        List<User> volunteers = userRepository.findByVolunteerTrue();
        assertThat(volunteers).hasSize(2);
        assertThat(volunteers).allMatch(User::isVolunteer);
    }

    @Test
    void save_shouldPersistUser() {
        User user = new User();
        user.setChatId(777L);
        user.setPhoneNumber("+7-999-123-44-55");
        User saved = userRepository.save(user);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getPhoneNumber()).isEqualTo("+7-999-123-44-55");
    }
}
