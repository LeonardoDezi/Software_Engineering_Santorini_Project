package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class PossibleMovesExtensions extends CardPower {

    public PossibleMovesExtensions (Game game, Card card) {
        super(game, card);
    }

    @Override
    public ArrayList<Square> getPossibleMoves(Builder builder) {

        ArrayList<Square> moves = basic.getPossibleMoves(builder);
        Square position;
        int i, j;

        switch (card.effects.moveOpponent) {

            case "swap":
                position = builder.getPosition();


                for (i = -1; i <= 1; i++) {

                    for (j = -1; j <= 1; j++) {

                        if (i != 0 || j != 0) {

                            int playerHeight = position.getLevel();

                            int a = position.x + i;
                            int b = position.y + j;

                            if (a >= 0 && a <= 5 && b >= 0 && b <= 5) {
                                int otherHeight = game.gameBoard.fullMap[a][b].getLevel();
                                if (playerHeight - otherHeight <= 1) {
                                    int newSquareValue = game.gameBoard.fullMap[a][b].getValue();
                                    if (newSquareValue == 1) {
                                        if (!(game.gameBoard.fullMap[a][b].getBuilder().getColour().equals(builder.getColour()))) {
                                            Square square = game.gameBoard.fullMap[a][b];
                                            moves.add(square);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                break;

            case "push":
                position = builder.getPosition();

                for (i = -1; i <= 1; i++) {
                    for (j = -1; j <= 1; j++) {
                        if (i != 0 || j != 0) {

                            int playerHeight = position.getLevel();

                            int a = position.x + i;
                            int b = position.y + j;

                            if (a >= 0 && a <= 5 && b >= 0 && b <= 5) {
                                int otherHeight = game.gameBoard.fullMap[a][b].getLevel();
                                if (playerHeight - otherHeight <= 1) {
                                    int newSquareValue = game.gameBoard.fullMap[a][b].getValue();
                                    if (newSquareValue == 1) {

                                        if (!(game.gameBoard.fullMap[a][b].getBuilder().getColour().equals(builder.getColour()))) {

                                            int otherBuilderHeight = game.gameBoard.fullMap[a][b].getLevel();
                                            Square otherBuilderPosition = game.gameBoard.fullMap[a][b];

                                            int c = otherBuilderPosition.x + i;
                                            int d = otherBuilderPosition.y + j;
                                            if (c >= 0 && c <= 5 && d >= 0 && d <= 5) {
                                                if (game.gameBoard.fullMap[c][d].getValue() == 0) {
                                                    if (game.gameBoard.fullMap[c][d].getLevel() - otherBuilderHeight <= 1) {
                                                        Square square = game.gameBoard.fullMap[a][b];
                                                        moves.add(square);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                break;


        }

        if (moves.isEmpty())
            moves = null;

        return moves;


    }
}