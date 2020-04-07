package Model;

import java.util.ArrayList;

public class Player {
    private boolean isPlayerTurn;
    //elemento di tipo carta
    private String playerID;
    private ArrayList<Builder> builders;
    private boolean isCardDealer;
    private String colour;
    private Builder piece;  // serve solo per getBuilder magari si può togliere
    //private Game game;  //mi sa che non serve

    public Player(String playerID /*, Game game*/) {

        this.playerID = playerID;
        //this.game = game;
        builders = new ArrayList<>();
        //come fare colour?
        isCardDealer = false;
        isPlayerTurn = false;

    }

    //serve ancora? si deve vedere come si implementerà Turn
    public void setIsTurn(boolean isPlayerTurn) {
        this.isPlayerTurn = isPlayerTurn;
    }  // aggiungere il parametro nell'UML


    //metodo getcard


    public String getPlayerID() {
        return playerID;
    }

    public Builder getBuilder(int BuilderId){

        try {
            piece = builders.get(BuilderId);   //forse lo posso scrivere return ma non sono sicuro
        }catch(NullPointerException e) {
            System.out.println("Error: The player" + playerID + "hasn't deployed his/her pieces yet.");
        }

        return piece;   //forse si può togliere

    }


    public int getBuilderSize(){     // o questo o restituiamo direttamente la lista non sono sicuro
        return builders.size();
    }

    //il controller potrà accedere a size()?
    //come facciamo con il builderid?


    public void takeCard(){/* TBD*/}

    public void addBuilder(int x, int y, Square position){
        builders.add(new Builder(x,y, position));
        //return builders.size() - 1;  // se magari vogliamo comunicare il numero della pedina che abbiamo appena messo
    }

}
