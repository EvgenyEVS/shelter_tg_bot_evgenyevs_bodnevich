package pro.sky.telegrambot.service;

import org.springframework.scripting.bsh.BshScriptEvaluator;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.repository.ShelterRepository;

@Service
public class ShelterService {

    private ShelterRepository shelterRepository;
    public ShelterService() {}
}
