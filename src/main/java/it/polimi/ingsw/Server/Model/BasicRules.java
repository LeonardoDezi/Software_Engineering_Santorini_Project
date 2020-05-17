package it.polimi.ingsw.Server.Model;



import java.util.ArrayList;

public class BasicRules{
    private Card card;
    private int maxBuild = 1;
    private int numMoves = 1;
    private int maxHeight;
    private Board board;
    private Game game;

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


    //problemi di controllo che non siano square qualunque?
    public void winCondition(Square initialPosition, Square finalPosition) {
        if (initialPosition.getLevel() == 2) {
            if (finalPosition.getLevel() == 3) {
                game.setGameEnded(true);
            }
        }
    }


    public ArrayList<Square> getPossibleMoves(Builder builder) {

        ArrayList<Square> possibleMoves = proximity(builder);
        possibleMoves = removeBuilderSquare(possibleMoves);
        possibleMoves = removeDomeSquare(possibleMoves);
        possibleMoves = removeTooHighPlaces(possibleMoves, builder);

        return possibleMoves;
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

    //TODO implement loseCondition
    public void loseCondition() {
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
                    
                    if (a >= 0 && a <= 5 && b >= 0 && b <= 5) {

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

    public ArrayList<Square> removeTooHighPlaces(ArrayList<Square> proximity, Builder builder) {
        Square position = builder.getPosition();
        int playerHeight = position.getLevel();

        for (int i = 0; i < proximity.size(); i++) {

            int otherHeight = proximity.get(i).getLevel();
            if (otherHeight - playerHeight > maxHeight) {     // testare
                proximity.remove(i);
                i--;
            }
        }
        return proximity;
    }

    public ArrayList<Square> getFreeSquares(){
        ArrayList<Square> freeSquares = null;
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                if(game.gameBoard.fullMap[i][j].getValue()==0){
                    Square square = game.gameBoard.fullMap[i][j];
                    freeSquares.add(square);
                }
            }
        }
    return freeSquares;
    }

}