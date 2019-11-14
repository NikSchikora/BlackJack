package de.on19.blackjack.viewer;

import de.on19.blackjack.cardmanager.Card;
import de.on19.blackjack.cardmanager.Deck;

public class CardViewer {

    public static void viewCard(Card card) {

        String value = getValueChar(card);
        String color = getColorChar(card);

        String ins1 = "│XX       │";
        String ins2 = "│    X    │";
        String ins3 = "│       XX│";

        ins1 = ins1.replace("XX", value);
        ins2 = ins2.replace("X", color);
        ins3 = ins3.replace("XX", value);


        System.out.println("   " + "┌─────────┐");
        System.out.println("   " + ins1);
        System.out.println("   " + "│         │");
        System.out.println("   " + ins2);
        System.out.println("   " + "│         │");
        System.out.println("   " + ins3);
        System.out.println("   " + "└─────────┘");
    }


    public static void displayDeck(Deck deck) {
        String line1 = "";
        String line2 = "";
        String line3 = "";
        String line4 = "";
        String line5 = "";
        String line6 = "";
        String line7 = "";
        for (Card c : deck.getCards()) {
            line1 += "   " + "┌─────────┐";
            line2 += "   " + "│" + getValueChar(c) + "       │";
            line3 += "   " + "│         │";
            line4 += "   " + "│    " + getColorChar(c) + "    │";
            line5 += "   " + "│         │";
            line6 += "   " + "│       " + getValueChar(c) + "│";
            line7 += "   " + "└─────────┘";
        }
        System.out.println(line1);
        System.out.println(line2);
        System.out.println(line3);
        System.out.println(line4);
        System.out.println(line5);
        System.out.println(line6);
        System.out.println(line7);
    }

    private static String getColorChar(Card card) {
        String color = "";
        switch(card.getColor()) {
            case SPADE:
                color = "♠";
                break;
            case DIAMOND:
                color = "♦";
                break;
            case HEART:
                color = "♥";
                break;
            case CLUB:
                color = "♣";
                break;
        }
        return color;
    }

    private static String getValueChar(Card card) {
        String value = "";
        switch (card.getValue()) {
            case ACE:
                value = "A ";
                break;
            case KING:
                value = "K ";
                break;
            case JACK:
                value = "B ";
                break;
            case QUEEN:
                value = "D ";
                break;
            case TEN:
                value = "10";
                break;
            default:
                value = card.getValue().getCardValue().toString() + " ";
                break;
        }
        return value;
    }

}
