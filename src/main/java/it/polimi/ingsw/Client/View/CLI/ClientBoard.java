package it.polimi.ingsw.Client.View.CLI;

/**
 * represents the Board in the Client side
 */
public class ClientBoard {

    /** represents the board, map is an array of Cells that represents where the pawns can move or build. */
    private static Cell[][] map;

    /** represents the number of rows of the board */
    private final int x = 5;

    /** represents the number of columns of the board */
    private final int y = 5;

    /**
     * creates and initializes the board.
     */
    public ClientBoard(){

        map = new Cell[x][y];
        for (int i=0; i<x; i++){
            for (int j=0; j<y; j++){
                map[i][j] = new Cell(i,j);
            }
        }
    }

    /**
     * this method gives back the map of the game
     * @return the client map
     */
    public Cell[][] getMap(){
        return map;
    }

    /**
     * this method gives back a cell given its coordinates
     * @param i is the x coordinate of the cell
     * @param j is the y coordinate of the cell
     * @return the cell of coordinates i and j
     */
    public static Cell getCell(int i, int j){
        return ClientBoard.map[i][j];
    }
}
