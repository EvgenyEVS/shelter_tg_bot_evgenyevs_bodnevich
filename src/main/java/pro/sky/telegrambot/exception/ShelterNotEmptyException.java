package pro.sky.telegrambot.exception;

public class ShelterNotEmptyException extends RuntimeException {
    public ShelterNotEmptyException(String message) {
        super(message);
    }
}