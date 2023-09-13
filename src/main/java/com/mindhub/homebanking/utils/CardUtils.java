package com.mindhub.homebanking.utils;

import static com.mindhub.homebanking.controllers.CardController.getRandomNumber;

public final class CardUtils {
    public static String getCardNumber() {
        return getRandomNumber(1000, 10000) + "-" + getRandomNumber(1000, 10000) + "-" + getRandomNumber(1000, 10000) + "-" + getRandomNumber(1000, 10000);
    }

    public static int getCvvNumber() {
        int cvv = getRandomNumber(100, 1000);
        return cvv;
    }
}
