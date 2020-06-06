package it.polimi.ingsw.Server.Model;

import java.io.IOException;
import java.util.ArrayList;


/** represents the basic rules and methods followed and used by every player during the game */
public class BasicRules{
    /** represents the initial level where the worker has to be in order to make a winning movement */
    public static final int INITIALLEVEL = 2;
    /** represents the final level where the worker has to arrive in order to make a winning movement */
    public static final int FINALLEVEL = 3;
    /** represents the maximum level that a worker can climb, according to the basic rules */
    public static final int BASICMAXHEIGHT = 1;

    /** represents the maximum level that the worker can climb at the moment */
    private int maxHeight;

    /** represents the board where the game is being played */
    private final Board board;

    /** represents the game that is currently being played */
    private final Game game;

    /** variable used to save the value of maxHeight before its modification */
    private int previousMaxHeight = BASICMAXHEIGHT;

    /**
     * creates the basicRules object that will be used during this game.
     * @param board is the board where the game is being played.
     * @param game is the game that is currently being played
     */
    public BasicRules(Board board, Game game) {
        this.board = board;
        this.game = game;
        this.maxHeight = BASICMAXHEIGHT;
    }

    /**
     * @return variable used to save the value of maxHeight before its modification
     */
    public int getPreviousMaxHeight() { return previousMaxHeight; }

    /** used to set previousMaxHeight
     * @param num represents the maxHeight of BasicRules before the following modification by setMaxHeight()
     */
    public void setPreviousMaxHeight(int num){this.previousMaxHeight = num;}

    /** used to set maxHeight
     * @param maxHeight is the new maxHeight
     */
    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    /**
     * @return the value of current maxHeight
     */
    public int getMaxHeight() {
        return maxHeight;
    }

    /**
     * checks if the move made by the player results in its victory, according to the generic winning rules
     * @param player is the player that made the move passed to the method
     * @param initialPosition is the place where the worker used to be before the examined move
     * @param finalPosition is the place where the worker is after the move
     */

    public void winCondition(Player player, Square initialPosition, Square finalPosition) {
        if (initialPosition.getLevel() == INITIALLEVEL && initialPosition.getValue() == 0) {
            if (finalPosition.getLevel() == FINALLEVEL && finalPosition.getValue() == 1 && finalPosition.getBuilder().getColour().equals(player.getColour())) {
                game.setGameEnded(true);
                game.setWinningPlayer(player);
            }
        }
    }



    /**
     * is used to give the player all the available places where its worker can build.
     *
     * @param builder is the worker that can build in the places returned by the method.
     * @return an arrayList with all the possible places to build.
     */
    public ArrayList<Square> getBuildingRange(Builder builder) {

        ArrayList<Square> possibleBuilds = proximity(builder);
        possibleBuilds = removeBuilderSquare(possibleBuilds);
        possibleBuilds = removeDomeSquare(possibleBuilds);

        return possibleBuilds;

    }

    /**
     * used to have a list of all the squares that surround the worker, independently of their content.
     * @param builder is the worker of which we want the surrounding squares.
     * @return an arraylist containing all the surrounding squares.
     */
    public ArrayList<Square> proximity(Builder builder) {

        ArrayList<Square> proximity = new ArrayList<>();
        Square position = builder.getPosition();

        int i, j;
        for (i = -1; i <= 1; i++) {

            for (j = -1; j <= 1; j++) {

                if (i != 0 || j != 0) {
                    
                    int a = position.x + i;
                    int b = position.y + j;
                    
                    if (a >= 0 && a < Board.BOARDSIZEX && b >= 0 && b < Board.BOARDSIZEY) {

                        Square square = board.fullMap[a][b];
                        proximity.add(square);


                    }
                }
            }
        }
        
        return proximity;
        
    }


    /**
     * removes the squares inside the list proximity that contain a worker
     * @param proximity is the list of squares from which we will remove the ones containing a worker
     * @return the proximity list without the squares containing workers
     */
    public ArrayList<Square> removeBuilderSquare(ArrayList<Square> proximity) {

        for(int i = 0; i < proximity.size(); i++) {
            if (proximity.get(i).getValue() == 1) {
                proximity.remove(i);
                i--;
            }
        }

        return proximity;
    }

    /**
     * removes the squares inside the list proximity that contain a dome
     * @param proximity is the list of squares from which we will remove the ones containing a dome
     * @return the proximity list without the squares containing domes
     */
    public ArrayList<Square> removeDomeSquare(ArrayList<Square> proximity) {

        for (int i = 0; i < proximity.size(); i++) {
            if (proximity.get(i).getValue() == 2) {
                proximity.remove(i);
                i--;
            }
        }
        return proximity;
    }

    /**
     * based on the level of the worker, it removes the squares containing towers too high to be reached by the worker, according to
     * maxHeight of basicRules
     * @param proximity is the list of squares from which we will remove the ones containing towers too high
     * @param builder is the worker of which we want to know the possibleMoves
     * @return the proximity list without the squares containing too high towers
     */
    //proximity non toglie le caselle con cupole o pedine
    public ArrayList<Square> removeTooHighPlaces(ArrayList<Square> proximity, Builder builder) {
        Square position = builder.getPosition();
        int playerHeight = position.getLevel();

        for (int i = 0; i < proximity.size(); i++) {

            int otherHeight = proximity.get(i).getLevel();
            if (otherHeight - playerHeight > maxHeight) {
                proximity.remove(i);
                i--;
            }
        }
        return proximity;
    }

    /**
     * used to build a construction and then verify if this move can result in the player's victory
     * @param player is the player that makes the building
     * @param position is the square where the builing is made
     * @param isDome if true, indicates that the construction will be a dome
     */
    //potrebbe dare problemi quando il metodo viene eseguito e un giocatore ha già vinto
    public void build(Player player, Square position, boolean isDome) throws IOException {
        if(position != null) {
            board.build(position, isDome);
            WinPhase winPhase = new WinPhase(game);
            winPhase.checkBuild(player);
        }
    }

    /**
     * used to move a worker and then verify if this move can result in the player's victory
     * @param player is the player who owns the worker moved
     * @param initialPosition is the square where the worker is initially placed
     * @param position is the square where the worker is going to be.
     */
    //potrebbe dare problemi quando il metodo viene eseguito e un giocatore ha già vinto
    public void move(Player player, Square initialPosition, Square position) throws IOException {
        board.move(initialPosition, position);
        WinPhase winPhase = new WinPhase(game);
        winPhase.checkMovement(player, initialPosition, position);
        if(!(game.getGameEnded()))
            winCondition(player, initialPosition, position);
    }

    /**
     * inserts in a list all the squares of the board that do not contain a worker
     * @return the list of the squares that don't contain a worker
     */
    public ArrayList<Square> getFreeSquares(){
        ArrayList<Square> freeSquares = new ArrayList<>();
        for(int i = 0; i< Board.BOARDSIZEX; i++){
            for(int j = 0; j< Board.BOARDSIZEY; j++){
                if(game.gameBoard.fullMap[i][j].getValue()==0){
                    Square square = game.gameBoard.fullMap[i][j];
                    freeSquares.add(square);
                }
            }
        }
        return freeSquares;
    }


}