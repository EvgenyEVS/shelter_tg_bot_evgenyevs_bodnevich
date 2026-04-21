package pro.sky.telegrambot.unit.model.enums;

import org.junit.jupiter.api.Test;
import pro.sky.telegrambot.model.enums.Gender;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class GenderTest {

    @Test
    void shouldHaveCorrectValues() {
        assertThat(Gender.values()).containsExactlyInAnyOrder(Gender.MALE, Gender.FEMALE, Gender.UNKNOWN);
    }

    @Test
    void valueOf_shouldReturnCorrespondingEnum() {
        assertThat(Gender.valueOf("MALE")).isEqualTo(Gender.MALE);
        assertThat(Gender.valueOf("FEMALE")).isEqualTo(Gender.FEMALE).isEqualTo(Gender.FEMALE);
        assertThat(Gender.valueOf("UNKNOWN")).isEqualTo(Gender.UNKNOWN);
    }
}
