package Model;

import java.util.ArrayList;

public class Board {

    public  Square[][] fullMap; //static?

    private ArrayList<Observer> observerList;


    public Board() {
        fullMap = new Square[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                fullMap[i][j] = new Square(i, j);
            }
        }

    }


}
