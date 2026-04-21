package pro.sky.telegrambot.unit.model.enums;

import org.junit.jupiter.api.Test;
import pro.sky.telegrambot.model.enums.PetStatus;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PetStatusTest {

    @Test
    void shouldHaveCorrectValues() {
      assertThat(PetStatus.values()).containsExactlyInAnyOrder(PetStatus.AVAILABLE, PetStatus.ON_TRIAL, PetStatus.ADOPTED, PetStatus.NOT_AVAILABLE);
    }

    @Test
    void valueOf_shouldReturnCorrespondingEnum() {
        assertThat(PetStatus.valueOf("AVAILABLE")).isEqualTo(PetStatus.AVAILABLE);
        assertThat(PetStatus.valueOf("ON_TRIAL")).isEqualTo(PetStatus.ON_TRIAL);
        assertThat(PetStatus.valueOf("ADOPTED")).isEqualTo(PetStatus.ADOPTED);
        assertThat(PetStatus.valueOf("NOT_AVAILABLE")).isEqualTo(PetStatus.NOT_AVAILABLE);
    }
}
