package pro.sky.telegrambot.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.TelegramBot;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pro.sky.telegrambot.controller.PetController;
import pro.sky.telegrambot.dto.PetDto;
import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.model.enums.PetType;
import pro.sky.telegrambot.service.PetService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetService petService;

    @MockBean
    private TelegramBot telegramBot;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createPet_shouldReturnOk() throws Exception {
        PetDto dto = new PetDto(null, PetType.CAT, "Murka", null, null, false, null, null, null, null);
        Pet saved = new Pet();
        saved.setId(1L);
        when(petService.addPet(any(PetDto.class))).thenReturn(saved);

        mockMvc.perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getAllPets_shouldReturnList() throws Exception {
        when(petService.getAllPets()).thenReturn(java.util.List.of(new Pet()));
        mockMvc.perform(get("/pets"))
                .andExpect(status().isOk());
    }
}
