package it.polimi.ingsw.Client.View.CLI;

import it.polimi.ingsw.Client.View.ClientBoard;

public class CliBoard {

    public static void drawBoard(ClientBoard clientBoard){

        String firstLine = "╔════╤════╤════╤════╤════╗";
        String lastLine = "╚════╧════╧════╧════╧════╝";
        String inline = "╟────┼────┼────┼────┼────╢";
        String startLine = "║ ";
        String midLine = " │ ";
        String endLine = " ║";
        System.out.println("  1    2    3    4    5");
        System.out.println(firstLine);

        for(int i=0; i<5; i++){
            System.out.print(startLine);
            for(int j=0; j<5; j++){
                System.out.print(ClientBoard.getCell(i,j).drawCell(i,j) + Colour.RESET);
                if(j != 4) {
                    System.out.print(midLine);
                }
            }
            System.out.print(endLine);
            int x =i+1;
            System.out.println("  " + x);
            if(i != 4){
                System.out.println(inline);
            }
        }

        System.out.println(lastLine);

    }

}
