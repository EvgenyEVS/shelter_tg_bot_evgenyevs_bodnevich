package pro.sky.telegrambot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.dto.UserDto;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getOrCreateUser(Long chatId, com.pengrad.telegrambot.model.User tgUser) {
        return userRepository.findByChatId(chatId).orElseGet(() -> {
            User newUser = new User();
            newUser.setChatId(chatId);
            newUser.setTelegramUserName(tgUser.username());
            newUser.setFirstName(tgUser.firstName());
            newUser.setLastName(tgUser.lastName());
            newUser.setUserStatus(User.UserStatus.NON_ACTIVE);
            newUser.setVolunteer(false);
            newUser.setDialogState(User.UserDialogState.START);
            return userRepository.save(newUser);
        });
    }

    @Transactional
    public User createUser(UserDto dto) {
        User user = new User();
        mapDtoToEntity(dto, user);
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с таким id не найден: " + id));
    }

    public User getUserByChatId(Long chatId) {
        return userRepository.findByChatId(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с таким chatId не найден: " + chatId));
    }

    @Transactional
    public User updateUser(Long id, UserDto dto) {
        User user = getUserById(id);
        mapDtoToEntity(dto, user);
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> getVolunteers() {
        return userRepository.findByVolunteerTrue();
    }

    private void mapDtoToEntity(UserDto dto, User entity) {
        entity.setChatId(dto.getChatId());
        entity.setTelegramUserName(dto.getTelegramUserName());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setAge(dto.getAge());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setUserStatus(dto.getUserStatus() != null ? dto.getUserStatus() : User.UserStatus.NON_ACTIVE);
    }
}
