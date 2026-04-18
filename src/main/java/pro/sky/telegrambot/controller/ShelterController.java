package pro.sky.telegrambot.controller;

import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.dto.shelterDto.ShelterCreateDto;
import pro.sky.telegrambot.dto.shelterDto.ShelterResponseDto;
import pro.sky.telegrambot.model.Shelter;
import pro.sky.telegrambot.model.enums.PetType;
import pro.sky.telegrambot.service.ShelterService;

import java.util.List;

@RestController
@RequestMapping("/shelter")
public class ShelterController {

    private final ShelterService shelterService;

    public ShelterController(ShelterService shelterService) {
        this.shelterService = shelterService;
    }

    @PostMapping("/create")
    public Shelter createShelter(@RequestBody ShelterCreateDto createDto) {
        return shelterService.createShelter(createDto);
    }


    @GetMapping("/by_pet_type")
    public Shelter getShelterByPetType(PetType petType) {
        return shelterService.findShelterByPetType(petType);
    }

    @GetMapping("/all")
    public List<ShelterResponseDto> getAllShelters() {
        return shelterService.allShelters();
    }
}