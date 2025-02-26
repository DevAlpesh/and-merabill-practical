package com.practical.merabillpractical.data.module;

public class Payments {

    private int id;
    private double amount;
    private PaymentType paymentType;
    private String provider;
    private String transactionRef;

    public Payments(double amount, PaymentType paymentType, String provider, String transactionRef) {
        this.amount = amount;
        this.paymentType = paymentType;
        this.provider = provider;
        this.transactionRef = transactionRef;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }


    public double getAmount() {
        return amount;
    }

    public int getId() {
        return id;
    }

}
