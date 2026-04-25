package pro.sky.telegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.dto.PetDto;
import pro.sky.telegrambot.mapper.PetMapper;
import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.service.PetService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

//http://localhost:8080/swagger-ui/index.html

@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;
    private final PetMapper petMapper;

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Питомец создан",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @PostMapping
    @Operation(summary = "Создание питомца")
    public PetDto createPet(@Valid @RequestBody PetDto petDto) {
        Pet pet = petService.addPet(petDto);
        return petMapper.toDto(pet);
    }

    @GetMapping
    @Operation(summary = "Получить всех питомцев")
    public ResponseEntity<List<PetDto>> getAllPets() {
        List<PetDto> dtos = petService.getAllPets().stream()
                .map(petMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить питомца по ID")
    public ResponseEntity<PetDto> getById(@PathVariable Long id) {
        return petService.getPetById(id)
                .map(petMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cats")
    @Operation(summary = "Получить всех кошек")
    public ResponseEntity<List<PetDto>> getCats() {
        List<PetDto> dtos = petService.getCats().stream()
                .map(petMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/dogs")
    @Operation(summary = "Получить всех собак")
    public ResponseEntity<List<PetDto>> getDogs() {
        List<PetDto> dtos = petService.getDogs().stream()
                .map(petMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/unknown_type")
    @Operation(summary = "Получить всех с неизвестным типом")
    public ResponseEntity<List<PetDto>> getUnknown() {
        List<PetDto> dtos = petService.getUnknown().stream()
                .map(petMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Редактировать питомца")
    public PetDto updatePet(@PathVariable Long id, @Valid @RequestBody PetDto petDto) {
        Pet pet = petService.updatePet(id, petDto);
        return petMapper.toDto(pet);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить питомца")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        petService.deletePet(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/assign_shelter")
    @Operation(summary = "Автоматическое присвоение приюта по типу")
    public ResponseEntity<PetDto> assignShelter(@PathVariable Long id) {
        Pet updated = petService.automaticallyAssignShelterByPetType(id);
        return ResponseEntity.ok(petMapper.toDto(updated));
    }
}
