package pro.sky.telegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.dto.PetDto;
import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.service.PetService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

//http://localhost:8080/swagger-ui/index.html

@RestController
@RequestMapping("/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }


    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cat, hwo was created and saved in DB. " +
                            "In default, for creating Cat, you need write in\n" +
                            "      Cat's \"name\", \"birthday\", \"gender\".",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)

            )
    })
    @PostMapping
    @Operation(summary = "Создание питомца и добавление в БД. Обязательные поля: petType(CAT, DOG), имя.")
    public Pet createPet(
            @Valid
            @RequestBody
            PetDto petDto) {
        return petService.addPet(petDto);
    }

    @GetMapping()
    @Operation(summary = "Получить всех питомцев из БД")
    public ResponseEntity<List<Pet>> getAllPets() {
        return ResponseEntity.ok(petService.getAllPets());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить определенного питомца из БД по ID")
    public ResponseEntity<Pet> getById(@PathVariable Long id) {
        return petService.getPetById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cats")
    @Operation(summary = "Получить всех CAT из БД")
    public ResponseEntity<List<Pet>> getCats() {
        return ResponseEntity.ok(petService.getCats());
    }

    @GetMapping("/dogs")
    @Operation(summary = "Получить всех DOG из БД")
    public ResponseEntity<List<Pet>> getDogs() {
        return ResponseEntity.ok(petService.getDogs());
    }

    @GetMapping("/unknown_type")
    @Operation(summary = "Получить всех UNKNOWN из БД")
    public ResponseEntity<List<Pet>> getUnknown() {
        return ResponseEntity.ok(petService.getUnknown());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Редактировать питомца в БД по ID")
    public Pet updatePet(@PathVariable Long id, @Valid @RequestBody PetDto petDto) {
        return petService.updatePet(id, petDto);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Удаление питомца из БД")
    public String deletePet (@PathVariable Long id) {
       return petService.deletePet(id);
    }

    @PutMapping("/{id}/assign_shelter")
    @Operation(summary = "Автоматическое присваивание питомцу поля Shelter(приют) " +
            "в соответствии с присвоенным питомцу PetType. Поиск в БД по ID.")
    public ResponseEntity<Pet> assignShelter(@PathVariable Long id) {
        Pet updatePet = petService.automaticallyAssignShelterByPetType(id);
        return ResponseEntity.ok(updatePet);
    }

}
