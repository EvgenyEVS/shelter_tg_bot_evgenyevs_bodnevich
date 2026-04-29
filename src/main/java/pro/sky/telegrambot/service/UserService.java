package pro.sky.telegrambot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.telegrambot.dto.UserDto;
import pro.sky.telegrambot.mapper.UserMapper;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @CachePut(value = "userByChatId", key = "#result.chatId")
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
    @CacheEvict(value = {"userByChatId", "userById"}, allEntries = true)
    public User createUser(UserDto dto) {
        User user = userMapper.toEntity(dto);
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "allUsers", unless = "#result.isEmpty()")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "userById", key = "#id")
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с таким id не найден: " + id));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "userByChatId", key = "#chatId")
    public User getUserByChatId(Long chatId) {
        return userRepository.findByChatId(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с таким chatId не найден: " + chatId));
    }

    @Transactional
    @CacheEvict(value = "userByChatId", key = "#dto.chatId")
    public User updateUser(Long id, UserDto dto) {
        User user = getUserById(id);
        userMapper.updateFromDto(dto, user);
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        Long chatId = user.getChatId();
        userRepository.deleteById(id);
        clearUserCache(chatId);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "volunteers", unless = "#result.isEmpty()")
    public List<User> getVolunteers() {
        return userRepository.findByVolunteerTrue();
    }


    @CacheEvict(value = {"userByChatId", "userById", "allUsers", "volunteers"}, key = "#chatId")
    public void clearUserCache(Long chatId) {}
}
