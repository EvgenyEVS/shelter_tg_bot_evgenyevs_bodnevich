package pro.sky.telegrambot.integration.controller;

import com.pengrad.telegrambot.TelegramBot;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pro.sky.telegrambot.controller.AdoptionAdminController;
import pro.sky.telegrambot.service.AdoptionService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")

class AdoptionAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdoptionService adoptionService;

    @MockBean
    private TelegramBot telegramBot;

    @Test
    void extendProbation_returnsOk() throws Exception {
        mockMvc.perform(post("/admin/adoptions/1/extend?days=14"))
                .andExpect(status().isOk());
    }

    @Test
    void passProbation_returnsOk() throws Exception {
        mockMvc.perform(post("/admin/adoptions/1/pass"))
                .andExpect(status().isOk());
    }
}
