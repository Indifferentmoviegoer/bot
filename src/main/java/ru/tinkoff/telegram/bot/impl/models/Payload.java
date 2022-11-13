package ru.tinkoff.telegram.bot.impl.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Payload {
    private Clusters[] clusters;

    public Clusters[] getClusters() {
        return clusters;
    }

    public void setClusters(Clusters[] clusters) {
        this.clusters = clusters;
    }
}
