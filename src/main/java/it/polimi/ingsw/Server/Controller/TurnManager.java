package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Server.Model.*;

import javax.swing.*;
import java.util.ArrayList;

// received != null: quando il giocatore non fa alcuna mossa getMove mi deve restituire received= null



//public Envelope getBothMovementMove(ArrayList<Square> moves1, Builder builder1, ArrayList<Square> moves2, Builder builder2, Player player)
//public Envelope getMovementMove(ArrayList<Square> moves, Builder builder, Player player); P.S. potremmo accorparlo a
//                                                                          getBothMovementMove e mettere
//                                                                             moves2 e builder 2 a null
//public Envelope getBuildMove(ArrayList<Square> moves1, Builder builder, boolean buildDome, Player player);
//public Envelope getBothBuildMove(ArrayList<Square> moves1, Builder builder1, boolean buildDome1, ArrayList<Square> moves2, Builder builder2, boolean buildDome2);



public class TurnManager {


    private Game game;
    private ArrayList<Player> playerList;

    private SpecialPhase1 specialPhase1;
    private SpecialPhase2 specialPhase2;
    private SpecialPhase3 specialPhase3;
    private MovementPhase movementPhase;
    private BuildingPhase buildingPhase;

    //private NetList netList??



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
        //netList = new NetList(); ??
    }

    public void letsPlay(){

        Builder builder1;
        Builder builder2;
        ArrayList<Square> moves1;
        ArrayList<Square> moves2;

        Envelope received;

        Square lastPosition;


        while(!(game.getGameEnded())){

            for (int i=0; i < playerList.size(); i++) {  //succederà qualcosa se nel mentre rimuoviamo un giocatore?
                Player player = playerList.get(i);
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
                    //Envelope received =netInterface.getBothMovementMove(moves1, builder1, moves2, builder2, player) //player non mi è così necessario

                    if(/* received != null */)
                        specialPhase1.actionMethod(received.getBuilder(), received.getMove());

                    if(game.getGameEnded())
                        break;

                }

                moves1 = movementPhase.getMoves(player, builder1);
                moves2 = movementPhase.getMoves(player, builder2);


                if ( !(moves1.isEmpty()) || !(moves2.isEmpty()) ) {

//movementPhase
                    // received =netInterface.getBothMovementMove(moves1, builder1, moves2, builder2, player) @ensures mossa valida

                    lastPosition = received.getBuilder().getPosition();
                    movementPhase.actionMethod(received.getBuilder(), received.getMove());

                    if(game.getGameEnded())
                        break;





//specialPhase2
                    moves1 = specialPhase2.getMoves(player, received.getBuilder(), lastPosition );

                    if(moves1 != null) {

                        //received = netInterface.getMovementMove(moves1, received.getBuilder(), player);

                        if(received != null)
                            specialPhase2.actionMethod(received.getBuilder(), received.getMove());

                        if (game.getGameEnded())
                            break;
                    }

//buildingPhase

                    moves1 = buildingPhase.getMoves(player, received.getBuilder());
                    Boolean buildDome = player.getCard().parameters.buildDome;


                    if(player.getCard().parameters.buildingPhaseMoves.equals("askForFemale")){
                        if (received.getBuilder().sex.equals("female")) {
                            //received=netInterface.getBuildMove(moves1, builder1, true, player);
                        }else if(player.getBuilderSize() ==2){
                                Builder female = player.getFemale();
                                moves2 = game.getRules().getBuildingRange(female);
                                // received = netInterface.getBothBuildMove(moves1, received.getBuilder(), false, moves2, female, false);
                        }else
                            //received =netInterface.getBuildMove(moves1, received.getBuilder(), false, player);
                    }else {
                        //received = netInterface.getBuildMove(moves1, builder1, buildDome, player);   //il flag buildDome indica se il giocatore può scegliere di costruire
                    }


                    buildingPhase.actionMethod(received.getBuilder(), received.getMove(), received.getIsDome());
                    if (game.getGameEnded())
                        break;


//specialPhase3
                    lastPosition = received.getMove();
                    moves1 = specialPhase3.getMoves(player, received.getBuilder() , lastPosition);
                    //received = netInterface.getBuildMove(moves1, builder1, buildDome,player);

                    //gestire il caso in cui non restituisca mosse
                    specialPhase3.actionMethod(received.getBuilder(), received.getMove(), received.getIsDome());

                    if (game.getGameEnded())
                        break;


//loseCondition
                }else{

                    playerList.remove(i);
                    i--;
                    game.removePlayer(player);
                    if(playerList.size() == 1){
                        game.setWinningPlayer(playerList.get(0));
                        game.setGameEnded(true);
                        break;
                    }
                }
            }
        }
        endGame();
    }


    public void endGame(){
        //broadcast("La partita è finita. Ha vinto game.currentPlayer!!!);
    }


   /* public void addPlayers(Player player){

    } */

}

