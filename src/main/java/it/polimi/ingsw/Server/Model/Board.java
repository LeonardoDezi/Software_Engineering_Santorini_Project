package it.polimi.ingsw.Server.Model;

/** Represents the game board
 * @version 1.5
 * @since 1.0
 */


public class Board {
    private int completedTowers;

    public Square[][] fullMap;   // per ora lo faccio public, ma forse bisogna metterlo square. in quel caso, modificare deploybuilder
    /** represents the board, fullmap is an array of sqares that rapresents the cells where the builders can move or build.
     */


   // private ArrayList<it.polimi.ingsw.Server.Model.Observer> observerList;  SOLO PER TESTARE

    /** is the list of the observers of the board.
     */

    /**
     * creates and initializes the board.
     */
    public Board() {

        fullMap = new Square[5][5];
        completedTowers = 0;
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

        Builder tmp1 = pointA.getBuilder();
        Builder tmp2 = pointB.getBuilder();

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


    }

    /**
     * adds the new building or the new level on an existing building on the board.
     * @param point the coordinate of the cell where the new building is going to be.
     * @param isDome this boolean is used to signal if the new building is going to be a dome in a level different from the 4th.
     */
    public void build(Square point, boolean isDome){   //va corretto il metodo

        if(point != null) {

            if (point.getLevel() == 3) {
                isDome = true;
                completedTowers++;
            }

            if (isDome)   //necessario per via di Atlante
                point.setValue(2);   // 2 = cupola

            point.setLevel(point.getLevel() + 1);
        }

    }

}








