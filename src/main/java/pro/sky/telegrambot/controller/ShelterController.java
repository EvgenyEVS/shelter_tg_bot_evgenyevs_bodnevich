package pro.sky.telegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import liquibase.change.core.CreateTableChange;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.dto.shelterDto.ShelterContactsDto;
import pro.sky.telegrambot.dto.shelterDto.ShelterCreateDto;
import pro.sky.telegrambot.dto.shelterDto.ShelterGeneralInfoDto;
import pro.sky.telegrambot.dto.shelterDto.ShelterResponseDto;
import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.model.Shelter;
import pro.sky.telegrambot.model.enums.PetType;
import pro.sky.telegrambot.service.ShelterService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/shelter")
public class ShelterController {

    private final ShelterService shelterService;

    public ShelterController(ShelterService shelterService) {
        this.shelterService = shelterService;
    }

    @PostMapping("/create")
    @Operation(summary = "Создание приюта")
    public Shelter createShelter(@RequestBody ShelterCreateDto createDto) {
        return shelterService.createShelter(createDto);
    }

    @GetMapping("/by_pet_type")
    @Operation(summary = "Найти приют по PetType (CAT, DOG)")
    public Shelter getShelterByPetType(PetType petType) {
        return shelterService.findShelterByPetType(petType);
    }

    @GetMapping("/all")
    @Operation(summary = "Найти все приюты")
    public List<ShelterResponseDto> getAllShelters() {
        return shelterService.allShelters();
    }

    @GetMapping("/general_info")
    @Operation(summary = "Дать общую информацию о приюте по PetType (CAT, DOG)")
    public ShelterGeneralInfoDto getGeneralInfo(PetType petType) {
        return shelterService.getGeneralInfo(petType);
    }

    @GetMapping("/contacts")
    @Operation(summary = "Дать контакты приюта по PetType (CAT, DOG)")
    public ShelterContactsDto getContacts(PetType petType) {
        return shelterService.getContacts(petType);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Изменение приюта по ID")
    public ResponseEntity<ShelterResponseDto> updateShelter(@PathVariable Long id,
                                                            @Valid @RequestBody ShelterCreateDto createDto) {
        ShelterResponseDto responseDto = shelterService.updateShelter(id, createDto);

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление приюта по ID. Возможно только, если в приюте нет питомцев")
    public ResponseEntity<String> deleteShelter(@PathVariable Long id) {
        boolean deleted = shelterService.deleteShelterIfEmpty(id);

        if (deleted) {
            return ResponseEntity.ok("Приют с ID = " + id + " успешно удален из БД");
        } else return ResponseEntity.status(HttpStatus.CONFLICT).body(
                "Приют с ID = " + id + " не удален, т.к. содержит записи в поле Pets. Сначала очистите приют."
        );
    }

}