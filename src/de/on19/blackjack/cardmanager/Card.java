package de.on19.blackjack.cardmanager;

import de.on19.blackjack.utils.Color;
import de.on19.blackjack.utils.Value;

public class Card {

    private Color color;
    private Value value;

    public Card(Color color, Value value) {
        this.color = color;
        this.value = value;
    }

    public Color getColor() {
        return this.color;
    }

    public Value getValue() {
        return this.value;
    }

}
