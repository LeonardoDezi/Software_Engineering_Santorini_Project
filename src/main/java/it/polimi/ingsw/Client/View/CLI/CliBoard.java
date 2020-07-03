package it.polimi.ingsw.Client.View.CLI;

/**
 * represents the Board for a command line interface.
 */
public class CliBoard {

    /**
     * prints all the CliBoard
     * @param clientBoard is the Board contained by the Client
     */
    public static void drawBoard(ClientBoard clientBoard){

        String firstLine = "╔════╤════╤════╤════╤════╗";
        String lastLine = "╚════╧════╧════╧════╧════╝";
        String inline = "╟────┼────┼────┼────┼────╢";
        String startLine = "║ ";
        String midLine = " │ ";
        String endLine = " ║";
        System.out.println("  1    2    3    4    5");
        System.out.println(Colour.ANSI_CYAN + firstLine + Colour.RESET);

        for(int i=0; i<5; i++){
            System.out.print(Colour.ANSI_CYAN + startLine + Colour.RESET);
            for(int j=0; j<5; j++){
                System.out.print(clientBoard.getCell(i,j).drawCell(i,j) + Colour.RESET);
                if(j != 4) {
                    System.out.print(Colour.ANSI_CYAN + midLine + Colour.RESET);
                }
            }
            System.out.print(Colour.ANSI_CYAN + endLine + Colour.RESET);
            int x =i+1;
            System.out.println("  " + Colour.RESET + x );
            if(i != 4){
                System.out.println(Colour.ANSI_CYAN + inline + Colour.RESET);
            }
        }

        System.out.println(Colour.ANSI_CYAN + lastLine + Colour.RESET);

    }

}
