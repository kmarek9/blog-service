package pl.twojekursy.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class PeselValidatorTest {

    @ParameterizedTest
    @CsvSource({
            "0123456789, false",
            "012345678912, false",
            "abcdefghijk, false",
            "88010313518, true",
            "88010313517, false",
            "60101759382, true"
    })
    void givenPesel_whenIsPeselValid_thenExpectedResult(String pesel, boolean expectedIsValid) {
        //when
        boolean peselValid = PeselValidator.isPeselValid(pesel);

        // then
        assertThat(peselValid).isEqualTo(expectedIsValid);
    }
}