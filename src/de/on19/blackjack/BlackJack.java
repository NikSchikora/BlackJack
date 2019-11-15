package de.on19.blackjack;

import de.on19.blackjack.cardmanager.Card;
import de.on19.blackjack.cardmanager.Deck;
import de.on19.blackjack.viewer.CardViewer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class BlackJack {

    /*
    Declaration of game relevant variables
     */

    private static Logger gamelog;

    private static ArrayList<Player> players;
    private static Integer playerCount;
    private static Scanner scanner;
    private static Deck playingDeck;
    private static Player dealer;
    private static int dealerValue;
    private static boolean runGame;


    /*
    Main method contains game loop
     */
    public static void main(String[] args) {
        runGame = true;
        gamelog = Logger.getLogger("gamelog");
        FileHandler handler;
        try {
            handler = new FileHandler("/log/game.log");
            gamelog.addHandler(handler);
            SimpleFormatter formatter = new SimpleFormatter();
            handler.setFormatter(formatter);
            gamelog.info("Logger was successfully added!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //initializing variables
        players = new ArrayList<>();
        dealerValue = 0;
        playingDeck = new Deck();
        dealer = new Player("Dealer", 18);
        scanner = new Scanner(System.in);

        //Get the player count and start the player creation
        clearConsole();
        System.out.println("Mit wie vielen Spielern willst du spielen? (1-8 Spieler)");
        playerCount = scanner.nextInt();
        if(playerCount > 8) {
            System.out.println("Die Spieleranzahl wurde auf 8 gesetzt.");
            playerCount = 8;
        }
        gamelog.info("SET > playerCount = 8");
        createPlayers();

        //Game-Loop: While-loop with game relevant methods
        while(runGame) {
            betRound();
            cardRound();
            evaluateBets();
            resetValues();
            gamelog.info("INF > A Round was completed!");
        }

    }




    //Sleep the game for a certain amount of time
    private static void pauseGame(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        gamelog.info("INF > Game paused for " + millis + " milliseconds!");
    }


    public void test(){
        System.out.println("Test");
    }



    //Method for player creation
    private static void createPlayers() {
        String name = "";
        int age = 0;
        //tmp-counter for right display of player number
        int tmpCount= 1;
        while(playerCount > 0) {
            System.out.println("Wie ist der Name von Spieler " + tmpCount + "?");
            try {
                name = scanner.next();
            } catch (Exception e) {
                System.out.println("Fehler");
            }
            System.out.println("Wie alt bist du, " + name + "?");
            try {
                age = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Fehler");
            }
            if(age < 18) {
                System.out.println("Spielteilnahme erst ab 18 Jahren. Glücksspiel kann süchtig machen!\n" +
                        "Infos und Hilfe unter www.bzga.de");
                break;
            }
            //New player gets created with new values
            Player player = new Player(name, age);
            players.add(player);
            System.out.println("Der Spieler " + name + " wurde erfolgreich hinzugefügt!");
            playerCount--;
            tmpCount++;
            pauseGame(2000);
            clearConsole();
        }
        System.out.println("Alle Spieler wurden hinzugefügt!");
        pauseGame(2000);
        clearConsole();
        gamelog.info("ADD > all players added");
    }






    //Betting round for distributing player bets
    private static void betRound() {

        System.out.println("Die Wettrunde hat begonnen! \n" +
                "Bitte setzt nacheinander einen ganzzahligen Betrag:");
        double playerBet;
        //Iterating all players and ask them how much they want to bet
        for(Player player : players) {

            System.out.println(player.getName() + " ist mit setzen an der Reihe \n" +
                    "Du hast noch " + player.getBalance() + " DH$");
            //Next double = bet
            playerBet = scanner.nextDouble();

            //If input is higher than balance -> All in
            if(playerBet > player.getBalance()) {
                System.out.println("Du hast nicht genügend DH$. Dein verbleibendes Geld wurde gesetzt!");
                playerBet = player.getBalance();
            }
            //Bet ist being set through player object
            player.setBet(playerBet);
            System.out.println(player.getName() + " hat " + playerBet + " DH$ gesetzt!");
            pauseGame(2000);
            clearConsole();
        }
        System.out.println("Die Einsätze sind wie folgt:");
        for(Player player : players) {
            System.out.println(player.getName() + "   :  " + player.getBet());
        }
        gamelog.info("FNS > bet round finished successfully");
    }






    //Card round distributes cards and asks players if they want another card
    private static void cardRound() {
        //Clear the current deck and create a new, shuffled one
        playingDeck.getCards().clear();
        playingDeck.createFullDeck(players.size());
        playingDeck.shuffleDeck();
        //Cards get distributed to all players
        for(int i = 0; i < 2; i++) {
            for (Player player : players) {
                player.getHand().addCard(playingDeck.getCard());
                gamelog.info("INF > A card was added to the hand of player " + player.getName());
            }
            dealer.getHand().addCard(playingDeck.getCard());
            gamelog.info("INF > A card was added to the hand of the dealer");
        }
        //Display of the card distribution -> Info
        System.out.println("Die Kartenverteilung");
        for(Player player : players) {
            System.out.println(player.getName() + ": ");
            CardViewer.displayDeck(player.getHand());
            gamelog.info("INF > Card Deck of player " + player.getName() + " has been displayed!");
            System.out.println("\n");
            pauseGame(3000);
        }
        //First card of the dealer gets displayed
        System.out.println("\nDie erste Karte des Dealers: ");
        CardViewer.viewCard(dealer.getHand().getCards().get(0));
        pauseGame(3000);
        clearConsole();




        //Players are getting asked whether they want another card or not
        for(Player player : players) {
            System.out.println(player.getName() + " ist an der Reihe: \n" +
                    "Dein Kartenwert: " + player.getHand().getDeckValue());
            System.out.println("Deine Karten: ");
            CardViewer.displayDeck(player.getHand());
            pauseGame(6000);
            //boolean hit describes the ability to draw another card
            boolean hit = true;
            while (hit) {
                System.out.println("Willst du noch eine weitere Karte abeheben? (Ja oder Nein)");
                //User input gets switched
                switch (scanner.next().toLowerCase()) {
                    case "ja":
                        //The first card of the deck is added to the players hand
                        Card card = playingDeck.getCard();
                        player.getHand().addCard(card);
                        //The drawn card is displayed
                        System.out.println("Du hast eine Karte gezogen. Deine Hand: ");
                        CardViewer.displayDeck(player.getHand());
                        System.out.println("Dein neuer Kartenwert: " + player.getHand().getDeckValue());
                        //If the card value is over 21, the player can't draw another card
                        if(player.getHand().getDeckValue() > 21) {
                            hit = false;
                            System.out.println("Dein Kartenwert ist zu hoch. Du bist RAUS!");
                        }
                        pauseGame(3000);
                        break;
                    //Default value: player can't draw another card
                    default:
                        System.out.println(player.getName() + "'s finaler Kartenwert: " + player.getHand().getDeckValue());
                        clearConsole();
                        hit = false;
                        pauseGame(3000);
                        break;
                }
            }
            clearConsole();
        }

        //After all players have drawn cards, the dealer must play
        dealerValue = dealer.getHand().getDeckValue();
        System.out.println("Die Karten des Dealers");
        CardViewer.displayDeck(dealer.getHand());
        System.out.println("Der Kartenwert: " + dealerValue);
        //If the card value is under 17, the dealer must draw another card, if it's over 17, he can't draw another one
        while(dealerValue < 17) {
            Card card = playingDeck.getCard();
            dealer.getHand().addCard(card);
            System.out.println("Der Dealer hat noch eine Karte abgehoben:");
            dealerValue = dealer.getHand().getDeckValue();
            CardViewer.displayDeck(dealer.getHand());
            System.out.println("Der Kartenwert des Dealers: " + dealerValue);
            pauseGame(2000);
        }
        System.out.println("Der finale Kartenwert des Dealers: " + dealer.getHand().getDeckValue());
        pauseGame(3000);
        gamelog.info("FNS > card round finished successfully");
    }






    private static void evaluateBets() {
        gamelog.info("INF > Started evaluation of the bets");
        //Iterating players again to distribute the bets
        dealerValue = dealer.getHand().getDeckValue();
        for(Player player : players) {
            System.out.println("\n" +
                    "\n");
            int deckValue = player.getHand().getDeckValue();


            /*
            if(deckValue > 21) {
                System.out.println(player.getName() + " hat diese Runde verloren und verliert " + player.getBet() + " DH$");
                System.out.println(player.getName() + "'s neuer Kontostand: " + player.getBalance() + "DH$ (-" + player.getBet() + "DH$)");
            } else {
                if(dealerValue < deckValue) {
                    System.out.println(player.getName() + " hat gegen den Dealer gewonnen und erhält " + (player.getBet()*2) + " DH$.");
                    player.setBalance(player.getBalance() + (player.getBet()*2));
                    System.out.println(player.getName() + "'s neuer Kontostand: " + player.getBalance() + "DH$ (+" + player.getBet() + "DH$)");
                }
                if(dealerValue > deckValue && dealerValue <= 21) {
                    System.out.println(player.getName() + " hat gegen den Dealer verloren und verliert " + player.getBet() + " DH$");
                    System.out.println(player.getName() + "'s neuer Kontostand: " + player.getBalance() + "DH$ (-" + player.getBet() + "DH$)");
                }
                if(dealerValue == deckValue) {
                    if(dealerValue == 21) {
                        if(player.getHand().isTrippleSeven() && !(dealer.getHand().isTrippleSeven())) {
                            System.out.println("Du hast gegen den Dealer gewonnen! Du erhälst " + (player.getBet() *2) + "DH$.");
                            player.setBalance(player.getBet()*2);
                        } else if(player.getHand().isTrippleSeven() && dealer.getHand().isTrippleSeven()) {
                            System.out.println(player.getName() + "Hat ein Unentschieden erspielt: er erhält seinen Einsatz zurück!");
                            player.setBalance(player.getBalance() + player.getBet());
                        } else {
                            System.out.println(player.getName() + "Du hat gegen den Dealer verloren, er verliert " + player.getBet() + " DH$");
                        }
                    }
                    System.out.println("Unentschieden: " + player.getName() + " erhält seinen Einsatz zurück!");
                    player.setBalance(player.getBalance() + player.getBet());
                }
            }
            */

            if(dealerValue <= 21) {
                if(deckValue < dealerValue) {
                    System.out.println(player.getName() + " hat diese Runde verloren und verliert " + player.getBet() + " DH$");
                    System.out.println(player.getName() + "'s neuer Kontostand: " + player.getBalance() + "DH$ (-" + player.getBet() + "DH$)");
                } else if(dealerValue < deckValue && deckValue <= 21) {
                    System.out.println(player.getName() + " hat gegen den Dealer gewonnen und erhält " + (player.getBet() *2) + "DH$.");
                    player.setBalance(player.getBalance() + player.getBet()*2);
                } else if(dealerValue == deckValue) {
                    System.out.println("Unentschieden: " + player.getName() + " erhält seinen Einsatz zurück!");
                    player.setBalance(player.getBalance() + player.getBet());
                } else if(dealerValue == 21 && deckValue == 21) {
                    if(player.getHand().isTrippleSeven() && !(dealer.getHand().isTrippleSeven())) {
                        System.out.println(player.getName() + " hat gegen den Dealer gewonnen und erhält " + (player.getBet() *2) + "DH$.");
                        player.setBalance(player.getBet()*2);
                    } else if(player.getHand().isTrippleSeven() && dealer.getHand().isTrippleSeven()) {
                        System.out.println("Unentschieden: " + player.getName() + " erhält seinen Einsatz zurück!");
                        player.setBalance(player.getBalance() + player.getBet());
                    } else {
                        System.out.println(player.getName() + " hat diese Runde verloren und verliert " + player.getBet() + " DH$");
                        System.out.println(player.getName() + "'s neuer Kontostand: " + player.getBalance() + "DH$ (-" + player.getBet() + "DH$)");
                    }
                } else {
                    System.out.println(player.getName() + " hat diese Runde verloren und verliert " + player.getBet() + " DH$");
                    System.out.println(player.getName() + "'s neuer Kontostand: " + player.getBalance() + "DH$ (-" + player.getBet() + "DH$)");
                }
            } else {
                if(deckValue > 21) {
                    System.out.println(player.getName() + " hat diese Runde verloren und verliert " + player.getBet() + " DH$");
                    System.out.println(player.getName() + "'s neuer Kontostand: " + player.getBalance() + "DH$ (-" + player.getBet() + "DH$)");
                } else {
                    System.out.println(player.getName() + " hat gegen den Dealer gewonnen und erhält " + (player.getBet() *2) + "DH$.");
                    player.setBalance(player.getBalance() + player.getBet()*2);
                }
            }

            pauseGame(3000);
            if(player.getBalance() <= 0) {
                System.out.println(players.size());
                System.out.println("Der Spieler " + player.getName() + " wurde wegen zu wenig Guthaben aus dem Spiel entfernt!");
                gamelog.info("INF > The player " + player.getName() + " was removed from the game due to insufficient balance!");
                pauseGame(3000);
            }
        }

        Iterator<Player> it = players.iterator();
        while(it.hasNext()) {
            Player itPlayer = it.next();
            if(itPlayer.getBalance() <= 0) {
                it.remove();
                gamelog.info("INF > Player was removed");
            }
        }

        clearConsole();

        if(players.isEmpty()) {
            System.out.println("Alle Spieler sind aus dem Spiel entfernt. Möchtet ihr noch eine Runde Spielen dann schreibt start in die Konsole!");
            switch (scanner.next().toLowerCase()) {
                case "start":
                    System.out.println("Here we go again!");
                    BlackJack.main(null);
                    break;
                default:
                    System.out.println("Schade das ihr schon geht. Bis zum nächsten mal!");
                    runGame = false;
                    break;
            }
        }

        if(runGame == true) {
            System.out.println("Diese Runde ist vorbei. Die nächste Runde beginnt in Kürze");
            pauseGame(10000);
        }
        gamelog.info("FNS > bets evaluated successfully");
        gamelog.info("INF > Going to reset the values");
    }




    private static void resetValues() {
        gamelog.info("INF > Reset the values");
        if(!players.isEmpty()) {
            for (Player player : players) {
                player.setBet(0);
                player.getHand().getCards().clear();
            }
            dealer.getHand().getCards().clear();
            dealerValue = 0;
        }
        gamelog.info("INF > All player values have been reset!");
    }




    //Clearing the console
    private static void clearConsole() {
        for(int i = 0; i < 50; i++) {
            System.out.println(" \n");
        }
    }


}
