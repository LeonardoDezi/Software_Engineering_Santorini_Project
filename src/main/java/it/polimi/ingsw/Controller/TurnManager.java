package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TurnManager {
    private Player currentPlayer;
    private Game game;
    private ArrayList<Player> playerList;
    public boolean gameEnded = false;
    private SpecialPhase1 specialPhase1;
    private SpecialPhase2 specialPhase2;
    private SpecialPhase3 specialPhase3;
    private MovementPhase movementPhase;
    private BuildingPhase buildingPhase;

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

        while(!gameEnded){

            for (Player player : playerList) {

                currentPlayer = player;
                Card currentCard = currentPlayer.getCard();
                builder1 = currentPlayer.getBuilder(0);

                try {
                    builder2 = currentPlayer.getBuilder(1);
                }catch(ArrayIndexOutOfBoundsException e){
                    builder2 = null;
                }

                //special phase 1
                moves1 = specialPhase1.genericMethod(builder1, currentCard);
                moves2 = specialPhase1.genericMethod(builder2, currentCard);

                if (moves1 != null || moves2 != null) {
                    String message;
                    if (currentPlayer.getCard().name.equals("Prometeo")) {
                        message = "Vuoi costruire prima del movimento? Se lo fai non potrai salire di livello.";
                        //inviare mosse
                        //aspetto risposta
                        if (answer != null) {
                            //esegui mossa speciale
                            Rules rules = game.getRules();
                            rules.setMaxHeight(0);  //come facciamo poi a rimetterla uguale a 1?
                        }
                    } else {
                        //Caronte:
                        // message = vuoi cambiare la posizione dell'avversario?
                        board.move(square1, square2);
                    }
                    //


                }
//movement phase
                moves1 = movementPhase.getMoves(builder1);
                moves2 = movementPhase.getMoves(builder2);

                if (moves1 == null && moves2 == null) {
                    //loseCondition();
                }else{
                    //invio delle moves alla virual view
                    //verrà restituita una mossa con il relativo builder
                    // builder è il builder ottenuto con il return
                    lastPosition = builder.getPosition();
                    //magari lo racchiudiamo dentro un if così da risparmiarci questo passaggio per le carte che non hanno bisogno di movement
                    movementPhase.movement(builder, position);   //li diamo in ingresso il builder e lo square di destinazione
                    game.gameBoard.move(lastPosition, position);  //movimento effettivo
                }

                //la winCondition si mette qui
                // è necessaria una specialphase2 per Selene? come comunicare di quale builder vogliamo il range e cosa fare se
                // il female worker non può costruire?

                //specialMoves = specialPhase2.getMoves(builder, lastPosition);

                //Zeus: se deve costruire su se stesso lo farà nella building Phase e metterà lo square di build a null: modificare build() perchè non faccia niente
                //in questo caso

                //dovremmo memorizzarci i livelli dello square di partenza e di arrivo prima di costruire?

                moves1 = buildingPhase.getMoves(builder);
                //invio delle mosse
                //ricezione dello square
                buildingPhase.building(position);
                //
               // game.gameBoard.build(builder, x, y);  //voglio modificare build per mettere square al posto di x e y
               // l'ho messo in buildingPhase
                //specialPhase3


                //winPhase  // qua si controllano i wincondition delle carte speciali


                if (gameEnded) {
                    break;
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
