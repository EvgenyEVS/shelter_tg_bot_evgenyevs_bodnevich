package pro.sky.telegrambot.unit.model.enums;

import org.junit.jupiter.api.Test;
import pro.sky.telegrambot.model.enums.PetType;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PetTypeTest {

    @Test
    void fromString_shouldReturnCorrespondingEnum() {
        assertThat(PetType.valueOf("CAT")).isEqualTo(PetType.CAT);
        assertThat(PetType.valueOf("DOG")).isEqualTo(PetType.DOG);
    }

    @Test
    void fromString_invalid_returnsUNKNOWN() {
        assertThat(PetType.UNKNOWN).isEqualTo(PetType.UNKNOWN);
    }
}
