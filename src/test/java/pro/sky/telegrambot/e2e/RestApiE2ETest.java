package pro.sky.telegrambot.e2e;

import com.pengrad.telegrambot.TelegramBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import pro.sky.telegrambot.dto.PetDto;
import pro.sky.telegrambot.dto.ReportDto;
import pro.sky.telegrambot.dto.UserDto;
import pro.sky.telegrambot.dto.shelterDto.ShelterCreateDto;
import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.model.enums.PetType;
import pro.sky.telegrambot.model.enums.PetStatus;
import pro.sky.telegrambot.model.enums.Gender;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class RestApiE2ETest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private TelegramBot telegramBot;

    private Long createdUserId;
    private Long createdPetId;

    @BeforeEach
    void setUp() {
    }

    @Test
    void fullUserAndPetLifecycle() {
        // Создаем пользователя
        UserDto userDto = new UserDto(12345L, "@e2e_user", "E2E", "Test", 30, "+7-999-111-22-33", User.UserStatus.ORDINARY);
        ResponseEntity<User> createUserResponse = restTemplate.postForEntity("/users", userDto, User.class);
        assertThat(createUserResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        User createdUser = createUserResponse.getBody();
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getId()).isNotNull();
        createdUserId = createdUser.getId();

        // Получаем пользователя по ID
        ResponseEntity<User> getUserResponse = restTemplate.getForEntity("/users/{id}", User.class, createdUserId);
        assertThat(getUserResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getUserResponse.getBody().getFirstName()).isEqualTo("E2E");

        // Получаем пользователя по chatId
        ResponseEntity<User> getByChatIdResponse = restTemplate.getForEntity("/users/byChatId?chatId=12345", User.class);
        assertThat(getByChatIdResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getByChatIdResponse.getBody().getId()).isEqualTo(createdUserId);

        // Обновляем пользователя
        userDto.setFirstName("UpdatedE2E");
        HttpEntity<UserDto> updateEntity = new HttpEntity<>(userDto);
        ResponseEntity<User> updateResponse = restTemplate.exchange("/users/{id}", HttpMethod.PUT, updateEntity, User.class, createdUserId);
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getBody().getFirstName()).isEqualTo("UpdatedE2E");

        // Создаем питомца
        PetDto petDto = new PetDto(null, PetType.DOG, "E2EDog", LocalDate.of(2020, 1, 1),
                Gender.MALE, true, PetStatus.AVAILABLE, "Friendly dog", "Healthy", "None");
        ResponseEntity<Pet> createPetResponse = restTemplate.postForEntity("/pets", petDto, Pet.class);
        assertThat(createPetResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Pet createdPet = createPetResponse.getBody();
        assertThat(createdPet).isNotNull();
        assertThat(createdPet.getId()).isNotNull();
        createdPetId = createdPet.getId();

        // Получаем всех питомцев
        ResponseEntity<Pet[]> getAllPetsResponse = restTemplate.getForEntity("/pets", Pet[].class);
        assertThat(getAllPetsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getAllPetsResponse.getBody()).hasSizeGreaterThanOrEqualTo(1);

        // Получаем питомца по ID
        ResponseEntity<Pet> getPetResponse = restTemplate.getForEntity("/pets/{id}", Pet.class, createdPetId);
        assertThat(getPetResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getPetResponse.getBody().getPetName()).isEqualTo("E2EDog");

        // Обновляем питомца
        petDto = new PetDto(null, PetType.DOG, "UpdatedDog", LocalDate.of(2020, 1, 1),
                Gender.MALE, true, PetStatus.ADOPTED, "Very friendly", "Excellent", "None");
        HttpEntity<PetDto> updatePetEntity = new HttpEntity<>(petDto);
        ResponseEntity<Pet> updatePetResponse = restTemplate.exchange("/pets/{id}", HttpMethod.PUT, updatePetEntity, Pet.class, createdPetId);
        assertThat(updatePetResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updatePetResponse.getBody().getPetStatus()).isEqualTo(PetStatus.ADOPTED);

        // Удаляем пользователя
        restTemplate.delete("/users/{id}", createdUserId);
        ResponseEntity<User> getDeletedUser = restTemplate.getForEntity("/users/{id}", User.class, createdUserId);
        assertThat(getDeletedUser.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shelterCrudE2E() {
        // Создаем приют
        ShelterCreateDto shelterDto = new ShelterCreateDto("CAT", "ул. Кошачья, 1", "Кошачий рай",
                "10:00-18:00", "http://map.cat", "+7-777-111-22-33", "Не шуметь");
        ResponseEntity<pro.sky.telegrambot.model.Shelter> createResponse = restTemplate.postForEntity("/shelter/create", shelterDto, pro.sky.telegrambot.model.Shelter.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Long shelterId = createResponse.getBody().getId();

        // Получаем приют по типу
        ResponseEntity<pro.sky.telegrambot.model.Shelter> getByTypeResponse = restTemplate.getForEntity("/shelter/by_pet_type?petType=CAT", pro.sky.telegrambot.model.Shelter.class);
        assertThat(getByTypeResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getByTypeResponse.getBody().getId()).isEqualTo(shelterId);

        // Получаем все приюты
        ResponseEntity<pro.sky.telegrambot.dto.shelterDto.ShelterResponseDto[]> getAllResponse = restTemplate.getForEntity("/shelter/all", pro.sky.telegrambot.dto.shelterDto.ShelterResponseDto[].class);
        assertThat(getAllResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getAllResponse.getBody()).hasSizeGreaterThanOrEqualTo(1);

        // Получаем общую информацию
        ResponseEntity<pro.sky.telegrambot.dto.shelterDto.ShelterGeneralInfoDto> generalInfoResponse = restTemplate.getForEntity("/shelter/general_info?petType=CAT", pro.sky.telegrambot.dto.shelterDto.ShelterGeneralInfoDto.class);
        assertThat(generalInfoResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(generalInfoResponse.getBody().address()).contains("Кошачья");

        // Получаем контакты
        ResponseEntity<pro.sky.telegrambot.dto.shelterDto.ShelterContactsDto> contactsResponse = restTemplate.getForEntity("/shelter/contacts?petType=CAT", pro.sky.telegrambot.dto.shelterDto.ShelterContactsDto.class);
        assertThat(contactsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(contactsResponse.getBody().contacts()).isEqualTo("+7-777-111-22-33");
    }

    @Test
    void reportFlowE2E() throws Exception {
        // Сначала создаём пользователя
        UserDto userDto = new UserDto(999L, "@reporter", "Rep", "Ort", 25, "+7-999-555-44-33", User.UserStatus.ADOPTER);
        ResponseEntity<User> userResponse = restTemplate.postForEntity("/users", userDto, User.class);
        assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Long userId = userResponse.getBody().getId();

        // Получение необработанных отчётов (список пуст)
        ResponseEntity<ReportDto[]> unreviewed = restTemplate.getForEntity("/reports/unreviewed", ReportDto[].class);
        assertThat(unreviewed.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
