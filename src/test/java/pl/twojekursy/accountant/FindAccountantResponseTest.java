package pl.twojekursy.accountant;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FindAccountantResponseTest {

    @Test
    void givenAccountant_whenFrom_thenCorrectResponse() {
        // given
        long expectedId = 6L;
        String expectedName = "name4";
        Accountant accountant = Accountant.builder()
                .id(expectedId)
                .name(expectedName)
                .build();

        // when
        FindAccountantResponse result = FindAccountantResponse.from(accountant);

        // then
        assertNotNull(result);
        assertEquals(expectedId, result.getId());
        assertEquals(expectedName, result.getName());
    }
}