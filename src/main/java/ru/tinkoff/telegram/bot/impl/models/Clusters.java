package ru.tinkoff.telegram.bot.impl.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Clusters {
    private Points[] points;

    public Points[] getPoints() {
        return points;
    }

    public void setPoints(Points[] points) {
        this.points = points;
    }
}
