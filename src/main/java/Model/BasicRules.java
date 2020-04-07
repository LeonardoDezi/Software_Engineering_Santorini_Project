package Model;

import java.util.ArrayList;

public class BasicRules {
    private ArrayList<Square> range;
    private int maxNumMove;
    private int maxNumBuild;
    private int maxHeight;
    private boolean sameSquare;
    private Board board;
    private Game game;

    public BasicRules(Board board, Game game){
        this.board = board;
        this.game = game;
    }

    /**
     * is used to finalize a move.
     * @param builder is the selected builder that is moving.
     * @param x is the x coordinate where the builder wants to move.
     * @param y is the y coordinate where the builder wants to move.
     */
    public void movement(Builder builder, int x, int y){
        winCondition(builder, x, y);
        board.move(builder, x, y);
    }

    /**
     * is used to finalize the building of a new level.
     * @param x is the x coordinate where the player wants to build
     * @param y is the y coordinate where the player wants to build
     */
    public void building(int x, int y){
        board.build(x, y, false);
    }

    public void winCondition(Builder builder, int x, int y){
        Square actualPosition = builder.getPosition();
        if (actualPosition.getLevel() == 2) {
            if (board.fullMap[x][y].getLevel() == 3) {
                String colour = builder.getColour();
                int i=0;
                while(game.playerList.get(i) != null) {
                    if (game.playerList.get(i).colour.equals(colour)) {
                        endGame(game.playerList.get(i));
                    }
                    i++;
                }
            }
        }
    }

    /**
     * is used to give the player all the available moves.
     * @param player is the player that can move.
     * @return an ArrayList of all the possible moves of all the the builder of a player.
     */
    public ArrayList<Square> getMovementRange(Player player){
        ArrayList<Square> firstPossibleMoves;
        ArrayList<Square> secondPossibleMoves;
        firstPossibleMoves = getPossibleMoves(player.builders.get(0));
        try {
            secondPossibleMoves = getPossibleMoves(player.builders.get(1));
        }catch(NullPointerException e) {
            secondPossibleMoves = null;
        }

        // capire come ridare tutti e due i valori

        if(firstPossibleMoves.isEmpty() && secondPossibleMoves.isEmpty()){
            losecondition();  // ci mettiamo un exception da propagare al chiamante
        }

        return firstPossibleMoves;
    }

    public ArrayList<Square> getPossibleMoves(Builder builder) {
        ArrayList<Square> possibleMoves = new ArrayList<>();
        Square position = builder.getPosition();
        maxHeight = 1;
        int i, j;
        for (i = -1; i <= 1; i++) {
            for (j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                } //non fai niente se sei nella tua casella
                else {
                    int playerHeight = position.getLevel();
                    int otherHeight = board.fullMap[position.x + i][position.y + j].getLevel();
                    if (playerHeight - otherHeight <= maxHeight) {
                        int newSquareValue = board.fullMap[position.x + i][position.y + j].getValue();
                        if (newSquareValue == 0) {
                            Square square = board.fullMap[position.x + i][position.y + j];
                            possibleMoves.add(square);
                        }
                    }
                }
            }
        }

        return possibleMoves;
    }



    /**
     * is used to give the player all the available places to build.
     * @param builder is the builder thant can build.
     * @return an arrayList with all the possible places to build.
     */
    public ArrayList<Square> getBuildingRange(Builder builder){
        ArrayList<Square> possibleBuilds = new ArrayList<>();
        Square position = builder.getPosition();
        int i, j;
        for(i=-1; i<=1; i++)
        {
            for(j=-1; j<=1; j++){
                if(i==0 && j==0){}
                else{
                    int newSquareValue=board.fullMap[position.x + i][position.y +j].getValue();
                    if(newSquareValue == 0){
                        Square square=board.fullMap[position.x + i][position.y +j];
                        possibleBuilds.add(square);
                    }
                }
            }
        }
        return possibleBuilds;
    }



}











