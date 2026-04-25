package pro.sky.telegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.service.AdoptionService;

@RestController
@RequestMapping("/admin/adoptions")
@RequiredArgsConstructor
@Validated
public class AdoptionAdminController {
    private final AdoptionService adoptionService;

    @PostMapping("/{adoptionId}/extend")
    @Operation(summary = "Продлить испытательный срок")
    public void extendProbation(@PathVariable Long adoptionId, @RequestParam int days) {
        adoptionService.extendProbation(adoptionId, days);
    }

    @PostMapping("/{adoptionId}/pass")
    @Operation(summary = "Завершить испытательный срок успешно")
    public ResponseEntity<Void> passProbation(@PathVariable Long adoptionId) {
        adoptionService.passProbation(adoptionId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{adoptionId}/fail")
    @Operation(summary = "Завершить испытательный срок неудачей")
    public ResponseEntity<Void> failProbation(@PathVariable Long adoptionId) {
        adoptionService.failProbation(adoptionId);
        return ResponseEntity.ok().build();
    }
}
