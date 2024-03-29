package de.on19.blackjack.utils;

public enum Value {

    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    JACK(10),
    QUEEN(10),
    KING(10),
    ACE(11),
    SUPERACE(20);

    private Integer cardValue;

    Value(int cardValue) {
        this.cardValue = cardValue;
    }

    public Integer getCardValue() {
        return this.cardValue;
    }


}
