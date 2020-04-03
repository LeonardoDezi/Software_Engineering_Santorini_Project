package Model;

import java.util.ArrayList;

public class Game {
    private Board gameBoard;
    private ArrayList<Player> playerList;
    private ArrayList<Card> chosenCards;
    private Deck deck;     //lo lasciamo?
    private Dealer dealer;
    private Player player;     // si può togliere?

    public Game(){     //da mettere un identificativo partita?
        gameBoard = new Board();
        playerList = new ArrayList<>();
        chosenCards = new ArrayList<>();
    }

    public void addPlayer(String name){
        if(playerList.size() < 3) {
            player = new Player(name);
            playerList.add(player);
            return;
        }else{
            //TBD
        }
    }

    public void removePlayer(Player player){
        playerList.remove(player);
        return;
    }

    public void setDealer(){
        player = playerList.get(0);    // prende il primo giocatore
        dealer= new Dealer(player, this);
    }

    public Deck getDeck(){      // da aggiungere  nell'UML se vogliamo mettere la variabile private
        return deck;
    }

    public void addChosenCard(Card card){    // da aggiungere all'UML
        chosenCards.add(card);
        return;
    }
}
