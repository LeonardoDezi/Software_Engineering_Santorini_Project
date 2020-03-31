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
            player = new Player(name);   // forse posso eliminare la variabile player
            playerList.add(player);
            return;
        }else{
            //bisogna fargli fare qualcosa
        }
    }

    public void removePlayer(Player player){
        playerList.remove(player);
        return;
    }

    public void setDealer(){      //potremmo anche fare senza string
        player = playerList.get(0);    // prende il primo giocatore
        dealer= new Dealer(player, game);

    }

    public Deck getDeck(){      // da aggiungere  nell'UML se vogliamo mettere la variabile private
        return deck;
    }

    public addChoosenCard(Card card){
        chosenCards.add(card);
    }
}
