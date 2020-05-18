package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Server.Model.*;

import javax.swing.*;
import java.util.ArrayList;

public class TurnManager {

    //mi devo salvere il valore della netInterface per usarlo in MovementPhase

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


        Envelope received;


        Square lastPosition;


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
                    //netInterface.sendMessage()

                    //Envelope received =netInterface.getMove(moves1, builder1, moves2, builder2, player) //player non mi è così necessario

                    if(/* received.getMove()!=null */)
                        specialPhase1.actionMethod(received.getBuilder(), received.getMove());

                    if(game.getGameEnded())
                        break;

                }

                moves1 = movementPhase.getMoves(player, builder1);
                moves2 = movementPhase.getMoves(player, builder2);


                if ( !(moves1.isEmpty()) || !(moves2.isEmpty()) ) {

//movementPhase
                    // received =netInterface.getMove(moves1, builder1, moves2, builder2, player) @ensures mossa valida

                    lastPosition = received.getBuilder().getPosition();
                    movementPhase.actionMethod(received.getBuilder(), received.getMove());

                    if(game.getGameEnded())
                        break;





//specialPhase2
                    moves1 = specialPhase2.getMoves(player, received.getBuilder(), lastPosition );

                    if(moves1 != null) {

                        //received = netInterface.getMove(moves1, builder1, player);

                        //gestire il caso in cui non restituisca mosse
                        specialPhase2.actionMethod(received.getBuilder(), received.getMove());

                        if (game.getGameEnded())
                            break;
                    }

//buildingPhase

                    moves1 = buildingPhase.getMoves(player, received.getBuilder());
                    Boolean buildDome = player.getCard().parameters.buildDome;

                    /* da completare Selene
                    if(player.getCard().parameters.buildingPhaseMoves == "askForFemale"){
                        if(received.getBuilder().sex == "male" && player.getBuilderSize() == 2){

                            game.getRules().getBuildingRange()
                        }
                    } */

                    //received = netInterface.getMove(moves1, builder1, buildDome, player);   //il flag buildDome indica se il giocatore può scegliere di costruire

                    buildingPhase.actionMethod(received.getBuilder(), received.getMove(), received.getIsDome());
                    if (game.getGameEnded())
                        break;


//specialPhase3
                    lastPosition = received.getMove();
                    moves1 = specialPhase3.getMoves(player, received.getBuilder() , lastPosition);
                    //received = netInterface.getMove(moves1, builder1, player);

                    //gestire il caso in cui non restituisca mosse
                    specialPhase3.actionMethod(received.getBuilder(), received.getMove(), received.getIsDome());

                    if (game.getGameEnded())
                        break;


                }else{
                    //loseCondition();
                }



            }
        }

    }

    public void addPlayers(Player player){

    }

}

