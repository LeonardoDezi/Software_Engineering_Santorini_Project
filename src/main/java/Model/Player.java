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
        // come facciamo per questo colore?

    }


    public void setIsTurn(boolean isPlayerTurn) {
        this.isPlayerTurn = isPlayerTurn;
    }  // aggiungere il parametro nell'UML


    //metodo getcard, bisogna capire bene come gestire le carte per poter fare questa parte


    public String getPlayerID() {
        return playerID;
    }

    public void chooseBuilder(int id, int xMove, int yMove, int xBuild, int yBuild){
        if(isPlayerTurn == true){
            try {
                piece = builders.get(id);
            }catch(NullPointerException e) {         //mi pare sia NullPointer
                System.out.println("Error: The player" + playerID + "hasn't deployed his/her pieces yet.");
            }

            piece.play(xMove, yMove, xBuild, yBuild);   //aggiungo questo metodo così per prova
              // come capire che la partita è finita?

        }else{
            //decidere cosa scrivere qui
        }

    }

    //no no questo è da riscrivere: facciamo che il controller verifica che la casella e libera e in quel caso chiama questo metodo se no qui non funziona
    //il controller potrà accedere a size()?
    //come facciamo con il builderid?
    public void deployBuilder(int x, int y){   // credo sia meglio che li metta singolarmente
        if(isPlayerTurn == true) {
            if (builders.size() == 2) {  //ATTENZIONE: DOBBIAMO ACCERTARCI CHE size() sarà sempre o 0 o 2 in qualche modo
                System.out.println("Error:" + playerID + "has already deployed all the builders");   //non possiamo lasciare questo
                isPlayerTurn = false;   //secondo me qua ci sta. in qalche modo glielo dobbiamo dire che ha finito soprattutto quando vuole fare deploy quando è già stato fatto
            } else {
                try {
                    builders.add(new Builder(x, y));
                } catch (NullPointerException e) {
                    System.out.println("Error:" + playerID + " cannot deploy a builder in" + x + "," + y);  // non possiamo lasciare questo
                }
                System.out.println("Builder deployed");  //non possiamo lasciare questo
            }



        } else{
            // qualcosa la dobbiamo scrivere
        }
        return;
    }

    public void takeCard(){// TBD}

}
