package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;

public class BasicRules{
    private static final int INITIALLEVEL = 2;
    private static final int FINALLEVEL = 3;
    private int maxHeight;
    private final Board board;
    private final Game game;

    public BasicRules(Board board, Game game) {
        this.board = board;
        this.game = game;
        this.maxHeight = 1;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }


    //non controlla che siano square adiacenti
    public void winCondition(Player player, Square initialPosition, Square finalPosition) {
        if (initialPosition.getLevel() == INITIALLEVEL && initialPosition.getValue() == 0) {
            if (finalPosition.getLevel() == FINALLEVEL && finalPosition.getValue() == 1 && finalPosition.getBuilder().getColour().equals(player.getColour())) {
                game.setGameEnded(true);
                game.setWinningPlayer(player);
            }
        }
    }



    /**
     * is used to give the player all the available places to build.
     *
     * @param builder is the builder thant can build.
     * @return an arrayList with all the possible places to build.
     */



    public ArrayList<Square> getBuildingRange(Builder builder) {

        ArrayList<Square> possibleBuilds = proximity(builder);
        possibleBuilds = removeBuilderSquare(possibleBuilds);
        possibleBuilds = removeDomeSquare(possibleBuilds);

        return possibleBuilds;

    }


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
    
    public ArrayList<Square> removeBuilderSquare(ArrayList<Square> proximity) {

        for(int i = 0; i < proximity.size(); i++) {
            if (proximity.get(i).getValue() == 1) {
                proximity.remove(i);
                i--;
            }
        }

        return proximity;
    }


    public ArrayList<Square> removeDomeSquare(ArrayList<Square> proximity) {

        for (int i = 0; i < proximity.size(); i++) {
            if (proximity.get(i).getValue() == 2) {
                proximity.remove(i);
                i--;
            }
        }
        return proximity;
    }


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

    //potrebbe dare problemi quando il metodo viene eseguito e un giocatore ha già vinto
    public void build(Player player, Square position, boolean isDome){
        board.build(position, isDome);
        WinPhase winPhase= new WinPhase(game);
        winPhase.checkBuild(player);
    }

    //potrebbe dare problemi quando il metodo viene eseguito e un giocatore ha già vinto
    public void move(Player player, Square initialPosition, Square position){
        board.move(initialPosition, position);
        WinPhase winPhase = new WinPhase(game);
        winPhase.checkMovement(player, initialPosition, position);
        if(!(game.getGameEnded()))
            winCondition(player, initialPosition, position);
            
    }

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