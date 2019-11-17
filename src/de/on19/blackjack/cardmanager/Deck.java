package de.on19.blackjack.cardmanager;

import de.on19.blackjack.utils.Color;
import de.on19.blackjack.utils.Value;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {

    private ArrayList<Card> cards;

    public Deck() {
        this.cards = new ArrayList<Card>();
    }


    public void createFullDeck(int decks) {
        for(int i = 0; i < decks; i++) {
            for (Color color : Color.values()) {
                for (Value value : Value.values()) {
                    Card c = new Card(color, value);
                    this.cards.add(c);
                }
            }
        }
    }

    public ArrayList<Card> getCards() {
        return cards;
    }


    public Integer getDeckValue() {
        int deckValue = 0;
        for(Card c : this.cards) {
            deckValue += c.getValue().getCardValue();
        }
        if(deckValue > 21) {                //Sonderregel Ass, kann als eins oder elf gewertet werden
            for(Card c : cards) {
                if(c.getValue().equals(Value.ACE) && (deckValue > 21)) {
                    deckValue -= 10;
                }
            }
        }
        return deckValue;
    }


    public void shuffleDeck() {
        Collections.shuffle(this.cards);
    }

    public String toString() {
        String toString = "";
        int count= 0;
        for(Card c : cards) {
            toString += count + " " + c.getColor().toString() + " : " + c.getValue().toString() + "\n";
            count++;
        }
        return toString;
    }

    public void addCard(Card card) {
        this.cards.add(card);
    }

    public Card getCard() {
        Card tmp = cards.get(0);
        cards.remove(0);
        return tmp;
    }



    public boolean isTrippleSeven() {       //Sonderregel, drei mal Sieben gewinnt immer
        for(Card c : this.getCards()) {
            if(c.getValue() != Value.SEVEN) {
                return false;
            }
        }
        return true;
    }


}
