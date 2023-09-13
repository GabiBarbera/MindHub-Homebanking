package com.mindhub.homebanking;

import com.mindhub.homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class CardUtilsTest {
   private String cardNumber = CardUtils.getCardNumber();
   private  int cvv = CardUtils.getCvvNumber();
    @Test
    public void cardNumberIsCreated() {
        assertThat(cardNumber, is(not(emptyOrNullString())));
    }
    @Test
    public void cardNumberIsString() {
        assertThat(cardNumber, isA(String.class));
    }
    @Test
    public void cvvIsInteger() {
        assertThat(cvv, isA(int.class));
    }
    @Test
    public void existsCvv() {
        assertThat(cvv, is(notNullValue()));
    }
}