package pro.sky.telegrambot.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pro.sky.telegrambot.controller.ShelterController;
import pro.sky.telegrambot.dto.shelterDto.*;
import pro.sky.telegrambot.model.Shelter;
import pro.sky.telegrambot.model.enums.PetType;
import pro.sky.telegrambot.service.ShelterService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShelterController.class)
class ShelterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ShelterService shelterService;

    @Test
    void createShelter_shouldReturnSavedShelter() throws Exception {
        ShelterCreateDto dto = new ShelterCreateDto("CAT", "ул. Кошачья, 5", "Уютный приют для кошек",
                "10:00-18:00", "http://map.cat", "+7-777-111-22-33", "Не кормить с рук");
        Shelter saved = new Shelter();
        saved.setId(1L);
        saved.setPetType(PetType.CAT);
        saved.setAddress("ул. Кошачья, 5");
        when(shelterService.createShelter(any(ShelterCreateDto.class))).thenReturn(saved);

        mockMvc.perform(post("/shelter/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.petType").value("CAT"))
                .andExpect(jsonPath("$.address").value("ул. Кошачья, 5"));
    }

    @Test
    void getShelterByPetType_shouldReturnShelter() throws Exception {
        Shelter shelter = new Shelter();
        shelter.setId(2L);
        shelter.setPetType(PetType.DOG);
        shelter.setAddress("ул. Собачья, 10");
        when(shelterService.findShelterByPetType(PetType.DOG)).thenReturn(shelter);

        mockMvc.perform(get("/shelter/by_pet_type?petType=DOG"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.petType").value("DOG"));
    }

    @Test
    void getAllShelters_shouldReturnList() throws Exception {
        ShelterResponseDto dto1 = new ShelterResponseDto(1L, PetType.CAT, "Address1", "Info1",
                "9-18", "http://map1", "+7-111", "Safety1");
        ShelterResponseDto dto2 = new ShelterResponseDto(2L, PetType.DOG, "Address2", "Info2",
                "10-19", "http://map2", "+7-222", "Safety2");
        when(shelterService.allShelters()).thenReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/shelter/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].petType").value("DOG"));
    }

    @Test
    void getGeneralInfo_shouldReturnGeneralInfo() throws Exception {
        ShelterGeneralInfoDto info = new ShelterGeneralInfoDto("Best shelter ever", "ул. Центральная, 1");
        when(shelterService.getGeneralInfo(PetType.CAT)).thenReturn(info);

        mockMvc.perform(get("/shelter/general_info?petType=CAT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shelterInfo").value("Best shelter ever"))
                .andExpect(jsonPath("$.address").value("ул. Центральная, 1"));
    }

    @Test
    void getContacts_shouldReturnContacts() throws Exception {
        ShelterContactsDto contacts = new ShelterContactsDto("10:00-18:00", "http://route", "+7-777-777-77-77", "Be careful");
        when(shelterService.getContacts(PetType.DOG)).thenReturn(contacts);

        mockMvc.perform(get("/shelter/contacts?petType=DOG"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shelterSchedule").value("10:00-18:00"))
                .andExpect(jsonPath("$.contacts").value("+7-777-777-77-77"));
    }
}