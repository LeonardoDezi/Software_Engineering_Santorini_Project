package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;

public class Dealer extends Player {


    public Dealer(String name, String colour, Game game, Integer clientID) {
        super(name, colour, game, clientID);
    }

    // bisogna ancora fare test per chooseCards
    public void chooseCards (int card1, int card2, int card3){

        game.addChosenCard(card1);
        game.addChosenCard(card2);
        game.addChosenCard(card3);

    }
}
