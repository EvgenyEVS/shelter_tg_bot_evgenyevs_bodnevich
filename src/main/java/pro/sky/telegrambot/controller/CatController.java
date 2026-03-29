package pro.sky.telegrambot.controller;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.dto.CatCreateDto;
import pro.sky.telegrambot.model.Cat;
import pro.sky.telegrambot.repository.CatRepository;
import pro.sky.telegrambot.service.CatService;

import javax.validation.Valid;
import java.util.List;

//http://localhost:8080/swagger-ui/index.html

@RestController
@RequestMapping("/cats")
public class CatController implements PetController {

    CatService catService;

    public CatController(CatService catService) {
        this.catService = catService;
    }

    @PostMapping
    public Cat createCat(
            @Valid
            @RequestBody
            CatCreateDto catCreateDto) {
        return catService.addCat(catCreateDto);
    }

    @GetMapping()
    public ResponseEntity<List<Cat>> getAllCats () {
       return ResponseEntity.ok(catService.getAllCats());
    }

}
