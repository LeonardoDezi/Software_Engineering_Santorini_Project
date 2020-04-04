package Model;
/*04/04/2020
Ho sistemato move e build e messo dealer come sottoclasse di player. ho spostato deployBuilder in Game e varie altre funzionalità.
non ho ancora sistemato takeCard e non ho toccato la logica dei turni. molti attributi e metodi sono csmbiati rispetto all'Uml originale quindi dobbiamo
cambiarlo.
 */

import java.util.ArrayList;

public class Game {
    private Board gameBoard;
    private ArrayList<Player> playerList;
    private ArrayList<Card> chosenCards;
    private Deck deck;     //lo lasciamo?
    private Dealer dealer;
    private Player player;     // si può togliere?
    // identificatore numerico per differenziare le partite?
    private Square position;

    public Game(){     //da mettere un identificativo partita?
        gameBoard = new Board();
        playerList = new ArrayList<>();
        chosenCards = new ArrayList<>();
    }

    public void addPlayer(String name){
        if(playerList.size() < 3) {

            if(playerList.size()==0)  //ancora nessun giocatore presente
                playerList.add(new Dealer(name, this)); //il primo giocatore sarà sempre il dealer
            else
                playerList.add(new Player(name));
            return;

        }else{
            //TBD
        }
    }

    public void removePlayer(Player player){
        playerList.remove(player);
        return;
    }

    // da rifare

    public Deck getDeck() {      // da aggiungere  nell'UML se vogliamo mettere la variabile private
        return deck;
    }

    public void move(int playerIndex, int builderId, int x, int y){
        player = playerList.get(playerIndex);   // recupera giocatore
        gameBoard.move(player.getBuilder(builderId), x, y);
    }

    public void build (int playerIndex, int builderId, int x, int y, boolean isDome){
        player = playerList.get(playerIndex);   // recupera giocatore
        gameBoard.build(player.getBuilder(builderId), x, y, isDome);
    }

    public void addChosenCard(Card card){    // da aggiungere all'UML
        chosenCards.add(card);
        return;
    }


    // arrivati a questo punto siamo certi che la posizione data è possibile?
    public void deployBuilder(Player player, int x, int y){

        this.player = player;
        //if(isPlayerTurn == true) {   serve?

        if(player.getBuilderSize() == 2){   //ATTENZIONE: DOBBIAMO ACCERTARCI CHE size() sarà sempre o 0 o 2 in qualche modo
            System.out.println("Error:" + player.getPlayerID() + "has already deployed all the builders");   //non possiamo lasciare questo
            //    isPlayerTurn = false;   Serve? compito del controller magari?
        } else {
            position = gameBoard.fullMap[x][y];  //mettere fullMap public o private?
            if(position.getValue() == 1)
                System.out.println("Error: Square already occupied by another player");
            else
                player.addBuilder(x, y, position);   //aggiungi alla lista dei builder del giocatore
                System.out.println("Builder deployed");  //non possiamo lasciare questo
        }

    }

}
