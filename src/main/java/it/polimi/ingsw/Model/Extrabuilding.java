package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class Extrabuilding extends CardPower {

    public Extrabuilding(Game game, Card card) {
        super(game, card);
    }

    @Override
    public ArrayList<Square> getBuildingRange(Builder builder) {

            ArrayList<Square> possibleBuilds = basic.getBuildingRange(builder);
            Square position = builder.getPosition();
            int i, j;

            if (card.effects.samesquare = true){
                for (i = -1; i <= 1; i++){

                    for (j = -1; j <= 1; j++){

                        int a = position.x + i;
                        int b = position.y + j;
                        if (a >= 0 && a <= 5 && b >= 0 && b <= 5) {
                            int newSquareValue = game.gameBoard.fullMap[position.x + i][position.y + j].getValue();
                            if (newSquareValue == 0) {
                                Square square = game.gameBoard.fullMap[position.x +i][position.y + j];
                                possibleBuilds.add(square);
                            }
                        }
                    }
                }
            }

            if (card.effects.perimetersquare =true){
                for (i = -1; i <= 1; i++){

                    for (j = -1; j <= 1; j++){

                        int a = position.x + i;
                        int b = position.y + j;
                        if (a >= 1 && a <= 4 && b >= 1 && b <= 4) {
                            int newSquareValue = game.gameBoard.fullMap[position.x + i][position.y + j].getValue();
                            if (newSquareValue == 0) {
                                Square square = game.gameBoard.fullMap[position.x +i][position.y + j];
                                possibleBuilds.add(square);
                            }
                        }
                    }
                }
            }

            return possibleBuilds;

    }
}
