package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.VirtualView.NetInterface;

import java.io.IOException;

/** Represents the game board
 * @version 1.5
 * @since 1.0
 */


public class Board {

    /** represents the number of rows of the board */
    public static final int BOARDSIZEX = 5;

    /** represents the number of columns of the board */
    public static final int BOARDSIZEY = 5;

    /** represents the number of completed towers constructed during the game */
    protected int completedTowers;

    /** represents the board, fullmap is an array of squares that represents the cells where the builders can move or build. */
    protected Square[][] fullMap;

    private NetInterface netInterface;




    /**
     * creates and initializes the board.
     * @param netInterface is the reference to the netInterface used to update the clients;
     */
    public Board(NetInterface netInterface) {

        fullMap = new Square[BOARDSIZEX][BOARDSIZEY];
        completedTowers = 0;
        this.netInterface = netInterface;
        for (int i = 0; i < BOARDSIZEX; i++) {
            for (int j = 0; j < BOARDSIZEY; j++) {
                fullMap[i][j] = new Square(i, j);
            }
        }
    }



    /**
     * swaps the content of two squares
     * @param pointA is the first square.
     * @param pointB is the second square.
      */

    public void move(Square pointA, Square pointB) throws IOException {

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
        netInterface.updateBoard(pointA, pointB);
    }

    /**
     * adds a new construction on a cell of the board.
     * @param point the coordinate of the cell where the new construction is going to be.
     * @param isDome this boolean is used to signal if the new construction is going to be a dome or a simple block
     * If the level of point is 3, the new construction will automatically be a dome, and completedTowers will be updated.*/
    public void build(Square point, boolean isDome) throws IOException {

        if(point != null) {

            if (point.getLevel() == 3) {
                point.setValue(2);
                completedTowers++;
            } else if(isDome){
                point.setValue(2);   // 2 = cupola
                point.setLevel(point.getLevel() - 1);
            }

            point.setLevel(point.getLevel() + 1);
        }
        netInterface.updateBoard(point, null);

    }

    public Square getSquare(int x, int y){ return fullMap[x][y];}
}
