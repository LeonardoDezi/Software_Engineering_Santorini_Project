package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Server.Model.Game;

public class GameInitializer implements Runnable{

    String player1;

    public GameInitializer(String firstPlayer){
        this.player1=firstPlayer;
    }

    @Override
    public void run() {

        Game game = new Game();
        game.addPlayer(player1);
        TurnManager myGameManager = new TurnManager(game);

        //wait for other players


    }


}
