package Model;

import java.util.ArrayList;

public class Board<Square> { //nelle classi ho provato a scrivere le cose messe nell'UML,
    private Square[6][6] Fullmap; //ci sono cose di sintassi che bisogna guardare.
    private Player ArrayList playerlist; //qui non capisco perchè li segna rossi

    public Board{
        for (int i=0; i<6; i++){ //questa è l'inizializzazione della board, bisogna mettere le torri ai lati ma
            for(int j=0; j<6; j++){ //finchè non prende il collegamento con square non so come fare
                fullmap[i][j].x=i;
                fullmap[i][j].y=j;
            }
        }
    }




}
