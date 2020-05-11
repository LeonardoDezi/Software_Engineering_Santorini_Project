package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;

public class PossibleMovesExtensions extends CardPower {

    public PossibleMovesExtensions (Game game, Card card) {
        super(game, card);
    }

    @Override  /* Per zeus */
    public ArrayList<Square> proximity(Builder builder) {
        ArrayList<Square> proximity = new ArrayList<>();
        Square position = builder.getPosition();

        int i, j;
        for (i = -1; i <= 1; i++) {

            for (j = -1; j <= 1; j++) {

                    int a = position.x + i;
                    int b = position.y + j;

                    if (a >= 0 && a <= 5 && b >= 0 && b <= 5) {

                        Square square = game.gameBoard.fullMap[a][b];
                        proximity.add(square);

                }
            }
        }

        return proximity;

    }

    @Override
    public ArrayList<Square> getPossibleMoves(Builder builder) {

        ArrayList<Square> possibleMoves = proximity(builder);
        possibleMoves = removeDomeSquare(possibleMoves);
        possibleMoves = removeTooHighPlaces(possibleMoves, builder);

        return possibleMoves;
    }
}