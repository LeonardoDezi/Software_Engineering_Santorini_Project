package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TurnManager {
    private Player currentPlayer;
    private Game game;
    private ArrayList<Player> playerList;   //check
    public boolean gameEnded = false;
    private SpecialPhase1 specialPhase1;
    private SpecialPhase2 specialPhase2;
    private SpecialPhase3 specialPhase3;
    private MovementPhase movementPhase;
    private BuildingPhase buildingPhase;


    //dovremmo assicurarci che sia unico?
    //immagino serviranno delle modifiche per il multipartita
    public TurnManager(Game game){
        this.game = game;
        this.playerList = game.getPlayerList();
        specialPhase1 = new SpecialPhase1();
        specialPhase2 = new SpecialPhase2();
        specialPhase3 = new SpecialPhase3();
        buildingPhase = new BuildingPhase();
        movementPhase = new MovementPhase(game.getRules(), game.getBoard());
    }

    public void letsPlay(){

        Builder builder1;
        Builder builder2;
        ArrayList<Square> moves1;
        ArrayList<Square> moves2;



        ArrayList<Square> specialMoves;
        Square lastPosition;
        int levelEnd;
        int levelStart;

        while(!gameEnded){

            for (Player player : playerList) {  //succederà qualcosa se nel mentre rimuoviamo un giocatore

               // currentPlayer = player;  serve?
                Card currentCard = player.getCard();
                builder1 = player.getBuilder(0);

                try {
                    builder2 = player.getBuilder(1);
                }catch(ArrayIndexOutOfBoundsException e){
                    builder2 = null;
                }

                //special phase 1
                moves1 = specialPhase1.genericMethod(player, builder1);
                moves2 = specialPhase1.genericMethod(player, builder2);

                if (!(moves1.isEmpty()) || !(moves2.isEmpty())) {   // se almeno uno dei due array non è vuoto
                    String message;
                  /*  if (currentPlayer.getCard().name.equals("Prometeo")) {
                        message = "Vuoi costruire prima del movimento? Se lo fai non potrai salire di livello.";
                        //inviare mosse
                        //aspetto risposta
                        if (answer != null) {
                            //esegui mossa speciale
                            Rules rules = game.getRules();
                            rules.setMaxHeight(0);  //MOSSA IMPORTANTE
                        }
                    } else {
                        //Caronte:
                        // message = vuoi cambiare la posizione dell'avversario?
                        board.move(square1, square2);
                    }
                    //
                    */   //gestione della specialPhase1

                }

                moves1 = movementPhase.getMoves(player, builder1);
                moves2 = movementPhase.getMoves(player, builder2);


                if (moves1 != null || moves2 != null) {

//movementPhase
                    //invio delle moves alla virual view
                    //verrà restituita una mossa con il relativo builder
                    // builder è il builder ottenuto con il return
                    lastPosition = builder.getPosition();
                    //magari lo racchiudiamo dentro un if così da risparmiarci questo passaggio per le carte che non hanno bisogno di movement
                    movementPhase.movement(builder, position);   //li diamo in ingresso il builder e lo square di destinazione

                    levelEnd = position.getLevel();
                    levelStart =lastPosition.getLevel();

                    //la winCondition la metteremo alla fine per accorpare il tutto.
                    //Ci salveremo i valori dei livelli così che non vangano modificati

                    game.gameBoard.move(lastPosition, position);  //movimento effettivo

//specialPhase2
                    moves1 = specialPhase2.getMoves(builder, lastPosition /*card*/);
                    //if(moves1== null) oppure gli mandiamo null? Ricezione di cosa bisogna fare
                    specialPhase2.genericMethod(builder, position);

                    moves1 = buildingPhase.getMoves(builder);


                    // è necessaria una specialphase2 per Selene? come comunicare di quale builder vogliamo il range e cosa fare se

//buildingPhase

                    moves1 = buildingPhase.getMoves(builder);
                    //invio delle mosse
                    //ricezione dello square e l'isDome
                    buildingPhase.building(position, isDome);

//specialPhase3
                    moves1 = specialPhase3.getMoves(builder /*card*/, lastPosition);
                    //invio delle mosse
                    //ricezione dello square e l'isDome
                    specialPhase3.genericMethod(builder, position);

                    board.build(position, isDome);


//winPhase
                    winPhase.wincondition();

                    if (gameEnded) {
                        //finalMethod();
                    }


                }else{
                    //loseCondition();
                }





            }
        }
        /* while(!gameEnded)
    for(player in players)
        doSomething(); */
    }

    public void addPlayers(Player player){

    }

}
