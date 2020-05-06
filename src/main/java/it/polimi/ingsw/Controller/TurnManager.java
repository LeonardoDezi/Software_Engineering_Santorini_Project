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

                //non sono sicuro che sia strettamente necessaria una specialPhase2 ma non ho ancora controllato bene

                //specialMoves = specialPhase2.getMoves(builder, lastPosition);

                //fase di building
                game.gameBoard.build(builder, x, y);  //voglio modificare build per mettere square al posto di x e y

                //specialPhase3


                //winPhase  // qua si controllano i wincondition delle carte speciali
                basicRules.winCondition(lastPosition, position);  // qua si controllano i wincondition generali, validi per tutte le carte

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
