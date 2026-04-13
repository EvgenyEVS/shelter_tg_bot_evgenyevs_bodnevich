package pro.sky.telegrambot.controller;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pro.sky.telegrambot.dto.shelterDto.ShelterContactsDto;
import pro.sky.telegrambot.dto.shelterDto.ShelterGeneralInfoDto;
import pro.sky.telegrambot.model.Shelter;
import pro.sky.telegrambot.model.enums.PetType;
import pro.sky.telegrambot.service.ShelterService;

import javax.validation.Valid;

@Controller
@RequestMapping("/shelter")
public class ShelterController {

    ShelterService shelterService;

    public ShelterController(ShelterService shelterService) {
        this.shelterService = shelterService;
    }

    @PostMapping
    public Shelter createShelter(
            @Valid
            @RequestBody
            Shelter shelter) {
        return shelterService.createShelter(shelter);
    }

    @GetMapping()
    public Shelter getShelter(PetType petType) {
        return shelterService.getShelterByPetType(petType);
    }

    @GetMapping("/general_info")
    public ShelterGeneralInfoDto getGeneralInfo(PetType petType) {
        return shelterService.getGeneralInfo(petType);
    }

    @GetMapping("/contacts")
    public ShelterContactsDto get_contacts(PetType petType) {
        return shelterService.getContacts(petType);
    }

}
