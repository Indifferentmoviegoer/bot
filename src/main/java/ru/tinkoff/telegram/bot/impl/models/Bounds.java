package ru.tinkoff.telegram.bot.impl.models;

public class Bounds {
    private BottomLeft bottomLeft;
    private TopRight topRight;

    public BottomLeft getBottomLeft() {
        return bottomLeft;
    }

    public void setBottomLeft(BottomLeft bottomLeft) {
        this.bottomLeft = bottomLeft;
    }

    public TopRight getTopRight() {
        return topRight;
    }

    public void setTopRight(TopRight topRight) {
        this.topRight = topRight;
    }
}
