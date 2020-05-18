package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Server.Model.*;

import java.util.ArrayList;

public class TurnManager {

    private Game game;
    private ArrayList<Player> playerList;

    private SpecialPhase1 specialPhase1;
    private SpecialPhase2 specialPhase2;
    private SpecialPhase3 specialPhase3;
    private MovementPhase movementPhase;
    private BuildingPhase buildingPhase;
    private WinPhase winPhase;


    //dovremmo assicurarci che sia unico?
    //immagino serviranno delle modifiche per il multipartita
    public TurnManager(Game game){
        this.game = game;
        this.playerList = game.getPlayerList();
        specialPhase1 = new SpecialPhase1(game);
        specialPhase2 = new SpecialPhase2(game);
        specialPhase3 = new SpecialPhase3(game);
        buildingPhase = new BuildingPhase(game);
        movementPhase = new MovementPhase(game);
        winPhase = new WinPhase(game);
    }

    public void letsPlay(){

        Builder builder1;
        Builder builder2;
        ArrayList<Square> moves1;
        ArrayList<Square> moves2;



        Square lastPosition;
        int levelEnd;
        int levelStart;

        while(!(game.getGameEnded())){

            for (Player player : playerList) {  //succederà qualcosa se nel mentre rimuoviamo un giocatore?

                builder1 = player.getBuilder(0);

                try {
                    builder2 = player.getBuilder(1);
                }catch(ArrayIndexOutOfBoundsException e){
                    builder2 = null;
                }

//specialPhase1

                moves1 = specialPhase1.getMoves(player, builder1);
                moves2 = specialPhase1.getMoves(player, builder2);

                if (!(moves1.isEmpty()) || !(moves2.isEmpty())) {   // se almeno uno dei due array non è vuoto

                    //ricezione mossa
                    if(/* mossa valida) */)
                    specialPhase1.actionMethod(builder, position);

                    //ATTENZIONE ATHENA


                }

                moves1 = movementPhase.getMoves(player, builder1);
                moves2 = movementPhase.getMoves(player, builder2);


                if ( !(moves1.isEmpty()) || !(moves2.isEmpty()) ) {

//movementPhase
                    //invio delle moves alla virual view
                    //verrà restituita una mossa con il relativo builder
                    // builder è il builder ottenuto con il return
                    lastPosition = builder.getPosition();
                    //magari lo racchiudiamo dentro un if così da risparmiarci questo passaggio per le carte che non hanno bisogno di movement
                    movementPhase.actionMethod(builder, position);   //li diamo in ingresso il builder e lo square di destinazione

                    //salviamo i valori dei livelli in modo tale che non vengano successivamente modificati


                    game.getBoard().move(lastPosition, position);  //movimento effettivo
                    winPhase.winCheck(lastPosition, position);

//specialPhase2
                    moves1 = specialPhase2.getMoves(player, builder, lastPosition );
                    //if(moves1== null) oppure gli mandiamo null? Ricezione di cosa bisogna fare
                    specialPhase2.actionMethod(builder, position);
                    winPhase.winCheck(lastPosition, position);

                    if(game.getGameEnded())
                        break;


//buildingPhase

                    moves1 = buildingPhase.getMoves(player, builder);
                    //invio delle mosse
                    //ricezione dello square e l'isDome
                    buildingPhase.actionMethod(builder, position, isDome);


//specialPhase3
                    lastPosition = position;
                    moves1 = specialPhase3.getMoves(player, builder , lastPosition);
                    //invio delle mosse
                    //ricezione dello square e l'isDome

                    specialPhase3.actionMethod(builder, position, isDome);


                    winPhase.winCheck(lastPosition, position);

                    if (game.getGameEnded()) {
                        //finalMethod();
                    }


                }else{
                    //loseCondition();
                }



            }
        }

    }

    public void addPlayers(Player player){

    }

}

