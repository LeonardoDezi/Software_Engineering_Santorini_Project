package Model;

import java.util.ArrayList;

public class Board { //nelle classi ho provato a scrivere le cose messe nell'UML,
    private Square[][] fullMap; //ci sono cose di sintassi che bisogna guardare.
    private ArrayList<Player> playerlist;

    public Board(){
        fullMap = new Square[6][6];
        for (int i=0; i<6; i++){ //questa è l'inizializzazione della board, bisogna mettere le torri ai lati ma
            for(int j=0; j<6; j++){ //finchè non prende il collegamento con square non so come fare
                fullMap[i][j] = new Square(i, j);
            }
        }
    }
    public Notify(){
        
    }



}
