package de.on19.blackjack;

import de.on19.blackjack.cardmanager.Deck;

public class Player {

    private String name;
    private Integer age;
    private double balance, bet;
    private Deck hand;

    public Player(String name, Integer age) {
        this.name = name;
        this.age = age;
        this.balance = 500.00;
        this.bet = 0;
        this.hand = new Deck();
    }

    public String getName() {
        return this.name;
    }

    public Integer getAge() {
        return this.age;
    }

    public double getBalance() {
        return this.balance;
    }

    public void setBalance(double newBalance) {
        this.balance = newBalance;
    }

    public double getBet() {
        return this.bet;
    }

    public void setBet(double bet) {
        this.balance -= bet;
        this.bet = bet;
    }

    public Deck getHand() {
        return this.hand;
    }

}
