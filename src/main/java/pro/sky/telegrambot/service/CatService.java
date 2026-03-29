package pro.sky.telegrambot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.dto.CatCreateDto;
import pro.sky.telegrambot.model.Cat;
import pro.sky.telegrambot.repository.CatRepository;

@Service
public class CatService {

    private final CatRepository catRepository;

    @Autowired
    public CatService(CatRepository catRepository) {this.catRepository = catRepository;}

    public Cat addCat(CatCreateDto catCreateDto) {

        Cat cat = new Cat();
        cat.setName(catCreateDto.name());
        cat.setBirthDay(catCreateDto.birthDay());
        cat.setGender(catCreateDto.gender());

        Cat savedCat = catRepository.save(cat);
        return savedCat;
    }
}
