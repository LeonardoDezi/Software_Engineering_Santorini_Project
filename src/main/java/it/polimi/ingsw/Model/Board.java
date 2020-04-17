package it.polimi.ingsw.Model;

import java.util.ArrayList;

/** Represents the game board
 * @version 1.5
 * @since 1.0
 */


public class Board {

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
     * moves a builder from a square to another.
     * @param builder is the builder that is moving.
     * @param x is the x coordinate of the future builders cell.
     * @param y is the y coordinat of the future builders cell.
      */



    public void move(Builder builder, int x, int y){

        pointA = builder.getPosition();    //builder's initial position
        pointB = fullMap[x][y];            //builder's arriving point

        int valueA = pointA.getValue();
        int valueB = pointB.getValue();


        pointB.setValue(valueA);              // value exchange
        pointA.setValue(valueB);


        Builder tmp = pointB.getBuilder();

        pointA.setBuilder(tmp);     // builder exchange. tmp will be null if the cell had been free
        pointB.setBuilder(builder);


        if(tmp!= null)
            tmp.setPosition(pointA); // in case of swap

        builder.setPosition(pointB);

 
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

        if(isDome == true)   //necessario per via di Atlante o lo modificheremo di conseguenza?
            pointB.setValue(2);   // 2 = cupola

        pointB.setLevel(pointB.getLevel() + 1);   // level in questo modo non indicherà il level della cupola. può dare
                                                  //  problemi alla GUI?

        return;

    }

}




