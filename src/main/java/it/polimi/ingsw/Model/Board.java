package it.polimi.ingsw.Model;

import java.util.ArrayList;

/** Represents the game board
 * @version 1.5
 * @since 1.0
 */


public class Board {
    private int completedTowers;

    public Square[][] fullMap;   // per ora lo faccio public, ma forse bisogna metterlo square. in quel caso, modificare deploybuilder
    /** represents the board, fullmap is an array of sqares that rapresents the cells where the builders can move or build.
     */
    private Square pointA;
    /** is the point where the builder that moves is before moving.
     */
    private Square pointB;
    /** is the point where the builder that moves is moving to.
     */
   // private ArrayList<it.polimi.ingsw.Model.Observer> observerList;  SOLO PER TESTARE

    /** is the list of the observers of the board.
     */

    /**
     * creates and initializes the board.
     */
    public Board() {

        fullMap = new Square[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                fullMap[i][j] = new Square(i, j);
            }
        }
    }


    //mossa verificata
    /**
     * swaps the content of two squares
     * @param pointA is the first square.
     * @param pointB is the second square.
      */

    public void move(Square pointA, Square pointB){

        Builder tmp1;
        Builder tmp2;
            tmp1 = pointA.getBuilder();
            tmp2 = pointB.getBuilder();
            pointB.setBuilder(tmp1);
            pointA.setBuilder(tmp2);

        if(tmp2 != null)
            tmp2.setPosition(pointA); // in case of swap
        if (tmp1 != null)
            tmp1.setPosition(pointB);

        int valueA = pointA.getValue();
        int valueB = pointB.getValue();
        pointB.setValue(valueA);              // value exchange
        pointA.setValue(valueB);


        return;

    }

    /**
     * adds the new building or the new level on an existing building on the board.
     * @param x the x coordinate of the cell where the new building is going to be.
     * @param y the y coordinate of the cell where the new building is going to be.
     * @param isDome this boolean is used to signal if the new building is going to be a dome in a level different from the 4th.
     */
    public void build(int x, int y, boolean isDome){   // mi sa che ci serve un parametro isDome qua

        pointB = fullMap[x][y];
        if(pointB.getLevel()==3){
            isDome = true;
            completedTowers ++;
        }

        if(isDome)   //necessario per via di Atlante
            pointB.setValue(2);   // 2 = cupola


        pointB.setLevel(pointB.getLevel() + 1);

    }

}








