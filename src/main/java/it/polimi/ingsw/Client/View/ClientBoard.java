package it.polimi.ingsw.Client.View;

public class ClientBoard {

    private static Cell[][] map;

    private final int x = 5;

    private final int y = 5;

    public ClientBoard(){

        map = new Cell[x][y];
        for (int i=0; i<x; i++){
            for (int j=0; j<y; j++){
                map[i][j] = new Cell(i,j);
            }
        }
    }

    public Cell[][] getMap(){
        return map;
    }

    public static Cell getCell(int i, int j){
        return ClientBoard.map[i][j];
    }
}
