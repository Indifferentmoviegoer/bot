package ru.tinkoff.telegram.bot.impl.models;

import java.util.ArrayList;

public class Filters {
    private ArrayList<String> banks;
    private Boolean showUnavailable = false;
    private ArrayList<String> currencies;

    public ArrayList<String> getBanks() {
        return banks;
    }

    public void setBanks(ArrayList<String> banks) {
        this.banks = banks;
    }

    public Boolean getShowUnavailable() {
        return showUnavailable;
    }

    public void setShowUnavailable(Boolean showUnavailable) {
        this.showUnavailable = showUnavailable;
    }

    public ArrayList<String> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(ArrayList<String> currencies) {
        this.currencies = currencies;
    }
}
