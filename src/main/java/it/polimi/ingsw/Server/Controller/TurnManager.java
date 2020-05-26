package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Server.Model.*;
import it.polimi.ingsw.Server.VirtualView.NetInterface;

import javax.swing.*;
import java.util.ArrayList;

// received != null: quando il giocatore non fa alcuna mossa getMove mi deve restituire received= null



//public Envelope getBothMovementMove(ArrayList<Square> moves1, Builder builder1, ArrayList<Square> moves2, Builder builder2, Player player)
//public Envelope getMovementMove(ArrayList<Square> moves, Builder builder, Player player); P.S. potremmo accorparlo a
//                                                                          getBothMovementMove e mettere
//                                                                             moves2 e builder 2 a null
//public Envelope getBuildMove(ArrayList<Square> moves1, Builder builder, boolean buildDome, Player player);
//public Envelope getBothBuildMove(ArrayList<Square> moves1, Builder builder1, boolean buildDome1, ArrayList<Square> moves2, Builder builder2, boolean buildDome2);


//loseMethod(): questo metodo dovrebbe inviare un messaggio al giocatore che ha perso comunicandogli la sconfitta,
//per poi chiudere la connessione. Non so come chiudere la connessione quindi non so specificare il prototipo

//broadcastMessage(String message): manda in broadcast il messaggio contenuto in message
//updateBoard(Board board): aggiorna in broadcast tutte le board dei giocatori

public class TurnManager {


    private final Game game;
    private final ArrayList<Player> playerList;
    private NetInterface netInterface;

    private SpecialPhase1 specialPhase1;
    private SpecialPhase2 specialPhase2;
    private SpecialPhase3 specialPhase3;
    private MovementPhase movementPhase;
    private BuildingPhase buildingPhase;

    //private NetList netList??



    //dovremmo assicurarci che sia unico?
    //immagino serviranno delle modifiche per il multipartita
    public TurnManager(Game game, NetInterface netInterface){
        this.game = game;
        this.netInterface = netInterface;
        this.playerList = game.getPlayerList();
        specialPhase1 = new SpecialPhase1(game);
        specialPhase2 = new SpecialPhase2(game);
        specialPhase3 = new SpecialPhase3(game);
        buildingPhase = new BuildingPhase(game);
        movementPhase = new MovementPhase(game);
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

                    //netInterface.sendMessage()
                    received =netInterface.getBothMovementMove(moves1, builder1, moves2, builder2, player);

                    if(received != null ) {
                        specialPhase1.actionMethod(received.getBuilder(), received.getMove());
                        //updateBoard(game.getBoard);
                    }
                    if(game.getGameEnded())
                        break;


                moves1 = movementPhase.getMoves(player, builder1);
                moves2 = movementPhase.getMoves(player, builder2);


                if ( !(moves1.isEmpty()) || !(moves2.isEmpty()) ) {

//movementPhase
                    received =netInterface.getBothMovementMove(moves1, builder1, moves2, builder2, player);

                    lastPosition = received.getBuilder().getPosition();
                    movementPhase.actionMethod(received.getBuilder(), received.getMove());
                    //updateBoard(game.getBoard);

                    if(game.getGameEnded())
                        break;





//specialPhase2
                    moves1 = specialPhase2.getMoves(player, received.getBuilder(), lastPosition );


                    received = netInterface.getMovementMove(moves1, received.getBuilder(), player);

                    if(received != null)
                        specialPhase2.actionMethod(received.getBuilder(), received.getMove());
                    //updateBoard(game.getBoard);

                    if (game.getGameEnded())
                            break;

//buildingPhase

                    moves1 = buildingPhase.getMoves(player, received.getBuilder());
                    Boolean buildDome = player.getCard().getParameters().buildDome;


                    if(player.getCard().getParameters().buildingPhaseMoves.equals("askForFemale")){
                        if (received.getBuilder().sex.equals("female")) {
                            received=netInterface.getBuildMove(moves1, builder1, true, player);
                        }else if(player.getBuilderSize() ==2){
                                Builder female = player.getFemale();
                                moves2 = game.getRules().getBuildingRange(female);
                                // received = netInterface.getBothBuildMove(moves1, received.getBuilder(), false, moves2, female, false);
                        }else
                            received =netInterface.getBuildMove(moves1, received.getBuilder(), false, player);
                    }else {
                        received = netInterface.getBuildMove(moves1, builder1, buildDome, player);
                    }


                    buildingPhase.actionMethod(received.getBuilder(), received.getMove(), received.getIsDome());
                    //updateBoard(game.getBoard);
                    if (game.getGameEnded())
                        break;


//specialPhase3
                    lastPosition = received.getMove();
                    moves1 = specialPhase3.getMoves(player, received.getBuilder() , lastPosition);
                    received = netInterface.getBuildMove(moves1, builder1, buildDome,player);

                    //gestire il caso in cui non restituisca mosse
                    specialPhase3.actionMethod(received.getBuilder(), received.getMove(), received.getIsDome());
                    //updateBoard(game.getBoard);

                    if (game.getGameEnded())
                        break;


//loseCondition
                }else{

                    playerList.remove(i);
                    i--;
                    game.removePlayer(player);
                    //loseMethod();
                    //sendMessage("Il giocatore" + player + "ha perso", null); //per mandare in broadcast il campo player è null
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
        //sendMessage("La partita è finita. Ha vinto game.currentPlayer!!!, null);
    }



}

