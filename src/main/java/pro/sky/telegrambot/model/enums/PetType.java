package pro.sky.telegrambot.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PetType {
    CAT,
    DOG,
    UNKNOWN;

    private static PetType fromString(String value) {
        if (value == null || value.isBlank()) {
            return UNKNOWN;
        }

        try {
            return PetType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}