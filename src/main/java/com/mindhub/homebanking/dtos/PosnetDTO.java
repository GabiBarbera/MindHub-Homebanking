package com.mindhub.homebanking.dtos;

public class PosnetDTO {
    private String number;
    private int cvv;
    private double amount;
    private String description;

    public PosnetDTO() {
    }

    public PosnetDTO(String number, int cvv, double amount, String description) {
        this.number = number;
        this.cvv = cvv;
        this.amount = amount;
        this.description = description;
    }

    public String getNumber() {
        return number;
    }

    public int getCvv() {
        return cvv;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}
