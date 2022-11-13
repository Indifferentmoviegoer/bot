package ru.tinkoff.telegram.bot.impl.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigInteger;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Limits {
    private String currency;
    private BigInteger max;
    private ArrayList<Integer> denominations;
    private BigInteger amount;


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigInteger getMax() {
        return max;
    }

    public void setMax(BigInteger max) {
        this.max = max;
    }

    public ArrayList<Integer> getDenominations() {
        return denominations;
    }

    public void setDenominations(ArrayList<Integer> denominations) {
        this.denominations = denominations;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }
}
