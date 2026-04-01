package pro.sky.telegrambot.controller;

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

//http://localhost:8080/swagger-ui/index.html

@RestController
@RequestMapping("/pets")
public class PetController {

    PetService petService;

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
    public Pet createPet(
            @Valid
            @RequestBody
            PetDto petDto) {
        return petService.addPet(petDto);
    }

    @GetMapping()
    public ResponseEntity<List<Pet>> getAllPets() {
        return ResponseEntity.ok(petService.getAllPets());
    }

}
