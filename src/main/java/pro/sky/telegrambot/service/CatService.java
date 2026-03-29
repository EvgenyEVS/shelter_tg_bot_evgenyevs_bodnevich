package pro.sky.telegrambot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.dto.CatCreateDto;
import pro.sky.telegrambot.model.Cat;
import pro.sky.telegrambot.repository.CatRepository;

import java.util.List;

@Service
public class CatService {

    private final CatRepository catRepository;

    @Autowired
    public CatService(CatRepository catRepository) {this.catRepository = catRepository;}

    /**
     * Creating Cat Entity and save in DB.
     * Validating data implemented in DTO.
     * In default, for creating Cat, you need write in
     * Cat's "name", "birthday", "gender".
     *
     * @param catCreateDto
     * @return Cat (savedCat)
     */
    public Cat addCat(CatCreateDto catCreateDto) {

        Cat cat = new Cat();
        cat.setName(catCreateDto.name());
        cat.setBirthDay(catCreateDto.birthDay());
        cat.setGender(catCreateDto.gender());

        Cat savedCat = catRepository.save(cat);
        return savedCat;
    }

    /**
     * Return List<Cat>, who contains all Cats saved in DB.
     * No parameters.
     * @return List<Cat>
     */
    public List<Cat> getAllCats() {
        return catRepository.findAll();
    }
}
