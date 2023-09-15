package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.models.Loan;

public class LoanUtils {
    public static Double calculateInterest(Loan loan) {
        int initialPercentage = 10;
        double totalInterest = 0.0;

        for (Integer payment : loan.getPayments()) {
            double interestPayment = payment * (initialPercentage / 100.0);
            totalInterest += interestPayment;
            initialPercentage += 5;
        }
        Double interest = totalInterest;
        return interest;
    }
}
