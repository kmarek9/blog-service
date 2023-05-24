package pl.twojekursy.bmi;

import org.assertj.core.data.Offset;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class BmiCalculatorTest {
    
    private BmiCalculator underTest = new BmiCalculator();
    
    @ParameterizedTest
    @CsvSource({
            "50, 190, 13.85, NIEDOWAGA",
            "80, 180, 24.69, OK",
            "120, 200, 30, NADWAGA"
    })
    void givenWeightAndHeight_whenCalculate_thenExpectedBmi(Integer weight, Integer height,
                                                            double expectedBmi, BmiNote expectedBmiNote ) {

        // when
        BmiCalculation bmiCalculation = underTest.calculate(weight, height);

        // then
        assertThat(bmiCalculation).isNotNull();
        assertThat(bmiCalculation.bmi()).isEqualTo(expectedBmi, Offset.offset(0.005));
        assertThat(bmiCalculation.bmiNote()).isEqualTo(expectedBmiNote);
    }
}