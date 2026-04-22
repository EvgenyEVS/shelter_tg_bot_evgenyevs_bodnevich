package pro.sky.telegrambot.integration.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pro.sky.telegrambot.controller.ReportController;
import pro.sky.telegrambot.dto.ReportDto;
import pro.sky.telegrambot.service.ReportService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportController.class)
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @Test
    void getUnreviewedReports_returnsOk() throws Exception {
        when(reportService.getUnreviewedReports()).thenReturn(List.of(new ReportDto()));
        mockMvc.perform(get("/reports/unreviewed"))
                .andExpect(status().isOk());
    }

    @Test
    void markAsReviewed_returnsOk() throws Exception {
        mockMvc.perform(post("/reports/1/review"))
                .andExpect(status().isOk());
    }
}