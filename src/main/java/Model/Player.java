package Model;

import java.util.ArrayList;

public class Player {
    private boolean isPlayerTurn;
    //elemento di tipo carta
    private String playerID;
    private ArrayList<Builder> builders;
    private boolean isCardDealer;
    private String colour;
    private Builder piece;  // da aggiungere all'UML

    public Player(String playerID) {

        this.playerID = playerID;
        builders = new ArrayList<>();


    }


    public void setIsTurn(boolean isPlayerTurn) {
        this.isPlayerTurn = isPlayerTurn;
    }  // aggiungere il parametro nell'UML


    //metodo getcard


    public String getPlayerID() {
        return playerID;
    }

    public void chooseBuilder(int id, int xMove, int yMove, int xBuild, int yBuild, boolean isDome){
        if(isPlayerTurn == true){
            try {
                piece = builders.get(id);
            }catch(NullPointerException e) {
                System.out.println("Error: The player" + playerID + "hasn't deployed his/her pieces yet.");
            }

            piece.play(xMove, yMove, xBuild, yBuild, isDome);
              // come capire che la partita è finita?

        }else{
            //TBD
        }

    }


    //il controller potrà accedere a size()?
    //come facciamo con il builderid?
    public void deployBuilder(int x, int y){
        if(isPlayerTurn == true) {
            if (builders.size() == 2) {  //ATTENZIONE: DOBBIAMO ACCERTARCI CHE size() sarà sempre o 0 o 2 in qualche modo
                System.out.println("Error:" + playerID + "has already deployed all the builders");   //non possiamo lasciare questo
                isPlayerTurn = false;   //compito del controller magari?
            } else {
                builders.add(new Builder(x, y));
                System.out.println("Builder deployed");  //non possiamo lasciare questo
            }

        } else{
            // TBD
        }
        return;
    }

    public void takeCard(){/* TBD*/}

}
