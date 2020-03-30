package Model;

import java.util.ArrayList;

public class Board {

    public  Square[][] fullMap; //nelle classi ho provato a scrivere le cose messe nell'UML,
     //ci sono cose di sintassi che bisogna guardare.   // public o protected?
    private ArrayList<Observer> observerList;


    public Board(){
        fullMap = new Square[5][5];
        for (int i=0; i<6; i++){ //questa è l'inizializzazione della board, bisogna mettere le torri ai lati ma
            for(int j=0; j<6; j++){ //finchè non prende il collegamento con square non so come fare
                fullMap[i][j] = new Square(i, j);
        }
    }


    public void notify(){
        //TBD
    }



}
