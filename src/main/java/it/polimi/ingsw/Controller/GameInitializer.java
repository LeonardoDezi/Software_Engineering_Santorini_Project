package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

public class GameInitializer implements Runnable{

    String player1;

    public GameInitializer(String firstPlayer){
        this.player1=firstPlayer;
    }

    @Override
    public void run() {

        Game game = new Game();
        game.addPlayer(player1);
        TurnManager myGameManager = new TurnManager();

        //wait for other players


    }
}
