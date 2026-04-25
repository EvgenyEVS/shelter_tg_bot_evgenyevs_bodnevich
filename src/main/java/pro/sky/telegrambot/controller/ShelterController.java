package pro.sky.telegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.dto.shelterDto.ShelterContactsDto;
import pro.sky.telegrambot.dto.shelterDto.ShelterCreateDto;
import pro.sky.telegrambot.dto.shelterDto.ShelterGeneralInfoDto;
import pro.sky.telegrambot.dto.shelterDto.ShelterResponseDto;
import pro.sky.telegrambot.mapper.ShelterMapper;
import pro.sky.telegrambot.model.Shelter;
import pro.sky.telegrambot.model.enums.PetType;
import pro.sky.telegrambot.service.ShelterService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/shelter")
@RequiredArgsConstructor
public class ShelterController {

    private final ShelterService shelterService;
    private final ShelterMapper shelterMapper;

    @PostMapping("/create")
    @Operation(summary = "Создать приют")
    public ShelterResponseDto createShelter(@RequestBody ShelterCreateDto createDto) {
        Shelter shelter = shelterService.createShelter(createDto);
        return shelterMapper.toResponseDto(shelter);
    }

    @GetMapping("/by_pet_type")
    @Operation(summary = "Найти приют по PetType")
    public ShelterResponseDto getShelterByPetType(@RequestParam PetType petType) {
        Shelter shelter = shelterService.findShelterByPetType(petType);
        return shelterMapper.toResponseDto(shelter);
    }

    @GetMapping("/all")
    @Operation(summary = "Все приюты")
    public List<ShelterResponseDto> getAllShelters() {
        return shelterService.allShelters();
    }

    @GetMapping("/general_info")
    @Operation(summary = "Общая информация")
    public ShelterGeneralInfoDto getGeneralInfo(@RequestParam PetType petType) {
        return shelterService.getGeneralInfo(petType);
    }

    @GetMapping("/contacts")
    @Operation(summary = "Контакты")
    public ShelterContactsDto getContacts(@RequestParam PetType petType) {
        return shelterService.getContacts(petType);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить приют")
    public ResponseEntity<ShelterResponseDto> updateShelter(@PathVariable Long id,
                                                            @Valid @RequestBody ShelterCreateDto createDto) {
        ShelterResponseDto responseDto = shelterService.updateShelter(id, createDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить приют (только если нет питомцев)")
    public ResponseEntity<Void> deleteShelter(@PathVariable Long id) {
        shelterService.deleteShelterIfEmpty(id);
        return ResponseEntity.noContent().build();
    }
}