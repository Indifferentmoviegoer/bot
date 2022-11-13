package ru.tinkoff.telegram.bot.impl.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Points {
    private Brand brand;
    private String address;
    private Limits[] limits;

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Limits[] getLimits() {
        return limits;
    }

    public void setLimits(Limits[] limits) {
        this.limits = limits;
    }
}
