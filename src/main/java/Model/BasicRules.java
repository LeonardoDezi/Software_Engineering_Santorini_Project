package Model;

import java.util.ArrayList;

public class BasicRules {
    private ArrayList<Square> range;
    private int maxNumMove;
    private int maxNumBuild;
    private int maxHeight;
    private boolean sameSquare;
    private Board board;

    public BasicRules(Board board, Game game){
        BasicRules basicRules = new BasicRules(board, game);
    }

    /**
     * is used to finalize a move.
     * @param builder is the selected builder that is moving.
     * @param x is the x coordinate where the builder wants to move.
     * @param y is the y coordinate where the builder wants to move.
     */
    public void movement(Builder builder, int x, int y, Game game){
        wincondition(builder, x, y,game);
        board.move(builder, x, y);
    }

    /**
     * is used to finalize the building of a new level.
     * @param x is the x coordinate where the player wants to build
     * @param y is the y coordinate where the player wants to build
     * @param isDome signalises if the new level is a dome.
     */
    public void building(int x, int y, boolean isDome){
        isDome=false;
        board.build(x, y, isDome);
    }

    public void wincondition(Builder builder, int x, int y, Game game){
        Square actualPosition = builder.getPosition();
        if (actualPosition.getLevel() == 2) {
            if (board.fullMap[x][y].getLevel() == 3) {
                String colour = builder.getColour();
                int i=0;
                    while(game.playerList.get(i) != null) {
                        if (game.playerList.get(i).colour == colour) {
                            endGame(game.playerList.get(i));
                        }
                    i++;
                }
            }
        }
    }

    /**
     * is used to give the player all the available moves.
     * @param builder is the builder that can move.
     * @return an ArrayList of all the possible moves of that builder.
     */
    public ArrayList<Square> getMovementRange(Player player){
        ArrayList<Square> firstPossibleMoves = new ArrayList<>();
        Square firstPosition = player.builders.get(0).getPosition();
        maxHeight=1;
        int i, j;
        for(i=-1; i<=1; i++)
        {
            for(j=-1; j<=1; j++){
                if(i==0 && j==0){} //non fai niente se sei nella tua casella
                else{
                    int playerHeight=firstPosition.getLevel();
                    int otherHeight=board.fullMap[firstPosition.x + i][firstPosition.y +j].getLevel();
                    if (playerHeight - otherHeight <= maxHeight){
                        int newSquareValue=board.fullMap[firstPosition.x + i][firstPosition.y +j].getValue();
                        if(newSquareValue == 0){
                            Square square=board.fullMap[firstPosition.x + i][firstPosition.y +j];
                            firstPossibleMoves.add(square);
                        }
                    }
                }
            }
        }

        ArrayList<Square> secondPossibleMoves = new ArrayList<>();
        Square secondPosition = player.builders.get(0).getPosition();
        maxHeight=1;
        int i, j;
        for(i=-1; i<=1; i++)
        {
            for(j=-1; j<=1; j++){
                if(i==0 && j==0){} //non fai niente se sei nella tua casella
                else{
                    int playerHeight=secondPosition.getLevel();
                    int otherHeight=board.fullMap[secondPosition.x + i][secondPosition.y +j].getLevel();
                    if (playerHeight - otherHeight <= maxHeight){
                        int newSquareValue=board.fullMap[secondPosition.x + i][secondPosition.y +j].getValue();
                        if(newSquareValue == 0){
                            Square square=board.fullMap[secondPosition.x + i][secondPosition.y +j];
                            secondPossibleMoves.add(square);
                        }
                    }
                }
            }
        }

        // capire come ridare tutti e due i valori

        if(firstPossibleMoves.isEmpty() && secondPossibleMoves.isEmpty()){
            losecondition();
            return firstPossibleMoves;
        }
        else
            return firstPossibleMoves;
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
