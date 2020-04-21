package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class BasicRules {
    private Card card;
    private int maxBuild=1;
    private int numMoves=1;
    private int maxHeight ;
    private Board board;
    private Game game;

    public BasicRules(Board board, Game game){
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
                     //   endGame(game.playerList.get(i));   SOLO PER TESTARE
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
        ArrayList<Square> firstSpecialMoves;
        ArrayList<Square> secondSpecialMoves;
        firstPossibleMoves = getPossibleMoves(player.builders.get(0));
        try {
            secondPossibleMoves = getPossibleMoves(player.builders.get(1));
        }catch(NullPointerException e) {
            secondPossibleMoves = null;
        }
        firstSpecialMoves=getSpecialMoves(player.builders.get(0), player);
        secondSpecialMoves=getSpecialMoves(player.builders.get(1), player);

        if(firstPossibleMoves.isEmpty() && secondPossibleMoves.isEmpty() && firstSpecialMoves.isEmpty() && secondSpecialMoves.isEmpty()){
           // losecondition();  // ci mettiamo un exception da propagare al chiamante SOLO PER TESTARE
        }

        return firstPossibleMoves; //ritornare tutti e 4 gli array
    }

    public ArrayList<Square> getPossibleMoves(Builder builder) {

        ArrayList<Square> possibleMoves = new ArrayList<>();
        Square position = builder.getPosition();

        int i, j;

        for (i = -1; i <= 1; i++) {

            for (j = -1; j <= 1; j++) {

                if (i != 0 || j != 0) {


                    int playerHeight = position.getLevel();

                    int a = position.x + i;
                    int b = position.y + j;

                    if (a >= 0 && a <= 5 && b >= 0 && b <= 5) {
                        int otherHeight = board.fullMap[a][b].getLevel();
                        if (playerHeight - otherHeight <= maxHeight) {
                            int newSquareValue = board.fullMap[a][b].getValue();
                            if (newSquareValue == 0) {
                                Square square = board.fullMap[a][b];
                                possibleMoves.add(square);
                            }
                        }
                    }
                }
            }
        }

        return possibleMoves;
    }

    public ArrayList<Square> getSpecialMoves(Builder builder, Player player){
        this.card=player.getCard();
        ArrayList<Square> specialMoves = new ArrayList<>();
        if(card.effects.movement.equals("swap")){
            Square position = builder.getPosition();
            int i, j;
            for (i = -1; i <= 1; i++) {
                for (j = -1; j <= 1; j++) {
                    if (i == 0 && j == 0) {
                    }
                    else {
                        int playerHeight = position.getLevel();
                        int otherHeight = board.fullMap[position.x + i][position.y + j].getLevel();
                        if (playerHeight - otherHeight <= maxHeight) {
                            int newSquareValue = board.fullMap[position.x + i][position.y + j].getValue();
                            if (newSquareValue == 1) {
                                if(board.fullMap[position.x+i][position.y+j].getBuilder().getColour()==builder.getColour()){} //do nothing
                                else{
                                    Square square = board.fullMap[position.x + i][position.y + j];
                                    specialMoves.add(square);
                                }
                            }
                        }
                    }
                }
            }
            if(specialMoves.isEmpty()){
                specialMoves=null;
            }
            return specialMoves;
        }
        else if(card.effects.movement.equals("1")){
         /*
            for (Square square: firstMove) {
                Builder shiftedBuilder=builder;
                shiftedBuilder.setPosition(square);
                getPossibleMoves(shiftedBuilder)
*/
            }
        else if(card.effects.movement.equals("push")){
            Square position = builder.getPosition();
            int i, j;
            for (i = -1; i <= 1; i++) {
                for (j = -1; j <= 1; j++) {
                    if (i == 0 && j == 0) {
                    }
                    else {
                        int playerHeight = position.getLevel();
                        int otherHeight = board.fullMap[position.x + i][position.y + j].getLevel();
                        if (playerHeight - otherHeight <= maxHeight) {
                            int newSquareValue = board.fullMap[position.x + i][position.y + j].getValue();
                            if (newSquareValue == 1) {
                                if(board.fullMap[position.x+i][position.y+j].getBuilder().getColour()==builder.getColour()){} //do nothing
                                else{
                                    Integer otherBuilderHeight =  board.fullMap[position.x+i][position.y+j].getLevel();
                                    Square otherBuilderPosition = board.fullMap[position.x+i][position.y+j];
                                    if(board.fullMap[otherBuilderPosition.x+i][otherBuilderPosition.y+j].getValue()==0){
                                        if (board.fullMap[otherBuilderPosition.x+i][otherBuilderPosition.y+j].getLevel()-otherBuilderHeight<=1){
                                            Square square = board.fullMap[position.x + i][position.y + j];
                                            specialMoves.add(square);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if(specialMoves.isEmpty()){
                specialMoves=null;
            }
            return specialMoves;
        }

        if(specialMoves.isEmpty()){
            specialMoves = null;
        }
        return specialMoves;
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
        for(i = -1; i <= 1; i++){

            for(j = -1; j <= 1; j++){

                if(i!=0 || j!=0) {
                    int a = position.x + i;
                    int b = position.x + j;
                    if (a >= 0 && a <= 5 && b >= 0 && b <= 5) {
                        int newSquareValue = board.fullMap[position.x + i][position.y + j].getValue();
                        if (newSquareValue == 0) {
                            Square square = board.fullMap[position.x + i][position.y + j];
                            possibleBuilds.add(square);
                        }
                    }
                }
            }
        }

        return possibleBuilds;

    }

}











