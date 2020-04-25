package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class BasicRules {
    private Card card;
    private int maxBuild=1;
    private int numMoves=1;
    private int maxHeight ;
    private Board board;
    private Game game;
    public boolean testFlag;  // ho messo questo questo flag giusto per testare winCondition


    //li ho messi come attributi in questo punto ma dobbiamo accertarci che un giocatore non andrà ad utilizzare
    //possiblemoves di mosse precedenti o che utilizzi array inizializzati come se fossero del suo turno

    private ArrayList<Square> firstPossibleMoves;   // forse queste vanno messe nel contorller e nei rispettivi metodi ci mettiamo getter e setter
    private ArrayList<Square> secondPossibleMoves;
    private ArrayList<Square> firstSpecialMoves;    // e se fondessimo gli array?
    private ArrayList<Square> secondSpecialMoves;

    public ArrayList<Square> getFirstPossibleMoves() {
        return firstPossibleMoves;
    }

    public BasicRules(Board board, Game game){
        this.board = board;
        this.game = game;
        this.maxHeight = 1;  // perchè qui e non sopra?
        this.testFlag = false;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    /**
     * is used to finalize a move.
     * @param builder is the selected builder that is moving.
     * @param x is the x coordinate where the builder wants to move.
     * @param y is the y coordinate where the builder wants to move.
     */
    public void movement(Builder builder, int x, int y){  //mossa già verificata in questo punto. ATTENZIONE!! le pedine pushate non devono passare per questo punto
        winCondition(builder, x, y);
        board.move(builder, x, y);
    }

    /**
     * is used to finalize the building of a new level.
     * @param x is the x coordinate where the player wants to build
     * @param y is the y coordinate where the player wants to build
     */
    public void building(int x, int y){
        board.build(x, y, false);
    }

    public void winCondition(Builder builder, int x, int y){
        Square actualPosition = builder.getPosition();
        if (actualPosition.getLevel() == 2) {
            if (board.fullMap[x][y].getLevel() == 3 ) {   // non serve controllare il Value dal momento che se non fosse una mossa valida non sarebbe in possibleMoves
                String colour = builder.getColour();

                for(int i = 0; i < game.playerList.size() && !testFlag; i++) {
                    if (game.playerList.get(i).colour.equals(colour)) {
                        //   endGame(game.playerList.get(i));   TBD
                        testFlag = true;
                    }
                }
            }
        }
        // e per quanto riguarda le condizioni speciali?
    }

    /**
     * is used to give the player all the available moves.
     * @param player is the player that can move.
     * @return an ArrayList of all the possible moves of all the the builder of a player.
     */
    public void getMovementRange(Player player){

        firstPossibleMoves = getPossibleMoves(player.builders.get(0));

        try {
            secondPossibleMoves = getPossibleMoves(player.builders.get(1));
        }catch(IndexOutOfBoundsException e) {
            secondPossibleMoves = null;    // alla fine non abbiamo più scelto carte che possono rimuovere pedine ma credo che possiamo comunque lasciarlo
        }

        firstSpecialMoves=getSpecialMoves(player.builders.get(0), player);

        try {
            secondSpecialMoves = getSpecialMoves(player.builders.get(1), player);
        }catch(IndexOutOfBoundsException e){
            secondSpecialMoves = null;
        }

        if(firstPossibleMoves == null  && firstSpecialMoves == null && secondPossibleMoves == null && secondSpecialMoves == null ){

           // losecondition();  // ci mettiamo un exception da propagare al chiamante SOLO PER TESTARE
            testFlag = true;
        }


    }

    //controllare che se due square hanno le stesse coordinate allora avranno lo stesso indirizzo
    public ArrayList<Square> getPossibleMoves(Builder builder) {

        ArrayList<Square> possibleMoves = new ArrayList<>();
        Square position = builder.getPosition();

        int i, j;

        for (i = -1; i <= 1; i++) {

            for (j = -1; j <= 1; j++) {

                if (i != 0 || j != 0) {

                    int a = position.x + i;
                    int b = position.y + j;

                    if (a >= 0 && a <= 5 && b >= 0 && b <= 5) {

                        int playerHeight = position.getLevel();
                        int otherHeight = board.fullMap[a][b].getLevel();
                        if (playerHeight - otherHeight <= maxHeight) {
                            int newSquareValue = board.fullMap[a][b].getValue();
                            if (newSquareValue == 0) {
                                Square square = board.fullMap[a][b];
                                possibleMoves.add(square);
                            }
                        }
                    }
                }
            }
        }

        if(possibleMoves.isEmpty())
            possibleMoves = null;

        return possibleMoves;
    }

    public ArrayList<Square> getSpecialMoves(Builder builder, Player player){

        this.card=player.getCard();

        ArrayList<Square> specialMoves = new ArrayList<>();


        if(card == null)    // l'ho messo solo per testare, poi dovremo cancellarlo
            return null;

        switch (card.effects.movement) {
            case "swap": {
                Square position = builder.getPosition();
                int i, j;
                for (i = -1; i <= 1; i++) {
                    for (j = -1; j <= 1; j++) {

                        if (i != 0 || j != 0) {

                            int a = position.x + i;
                            int b = position.y + j;

                            if (a >= 0 && a <= 5 && b >= 0 && b <= 5) {

                                int playerHeight = position.getLevel();
                                int otherHeight = board.fullMap[a][b].getLevel();

                                if (playerHeight - otherHeight <= maxHeight) {
                                    int newSquareValue = board.fullMap[a][b].getValue();
                                    if (newSquareValue == 1) {
                                        if (!(board.fullMap[a][b].getBuilder().getColour().equals(builder.getColour()))) { // scriviamola meglio va'
                                            Square square = board.fullMap[a][b];
                                            specialMoves.add(square);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (specialMoves.isEmpty())
                    specialMoves = null;

                return specialMoves;
            }

            case "1":
         /*
            for (Square square: firstMove) {
                Builder shiftedBuilder=builder;
                shiftedBuilder.setPosition(square);
                getPossibleMoves(shiftedBuilder)
            */
                break;
            case "push": {
                Square position = builder.getPosition();
                int i, j;
                for (i = -1; i <= 1; i++) {

                    for (j = -1; j <= 1; j++) {

                        if (i != 0 || j != 0) {

                            int a = position.x + i;
                            int b = position.y + j;

                            if (a >= 0 && a <= 5 && b >= 0 && b <= 5) {

                                int playerHeight = position.getLevel();
                                int otherHeight = board.fullMap[a][b].getLevel();

                                if (playerHeight - otherHeight <= maxHeight) {

                                    int newSquareValue = board.fullMap[a][b].getValue();

                                    if (newSquareValue == 1) {

                                        if (!(board.fullMap[a][b].getBuilder().getColour().equals(builder.getColour()))) {

                                            int otherBuilderHeight = board.fullMap[a][b].getLevel();
                                            Square otherBuilderPosition = board.fullMap[a][b];

                                            int c = otherBuilderPosition.x + i;
                                            int d = otherBuilderPosition.y + j;

                                            if (c >= 0 && c <= 5 && d >= 0 && d <= 5) {

                                                if (board.fullMap[c][d].getValue() == 0) {
                                                    if (board.fullMap[c][d].getLevel() - otherBuilderHeight <= 1) {
                                                        Square square = board.fullMap[a][b];
                                                        specialMoves.add(square);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (specialMoves.isEmpty()) {
                    specialMoves = null;
                }
                return specialMoves;
            }
        }

        if(specialMoves.isEmpty()){    // finora ritornano tutti nei loro rispettivi if_statements
            specialMoves = null;
        }
        return specialMoves;
    }


    /**
     * is used to give the player all the available places to build.
     * @param builder is the builder thant can build.
     * @return an arrayList with all the possible places to build.
     */
    public ArrayList<Square> getBuildingRange(Builder builder){

        ArrayList<Square> possibleBuilds = new ArrayList<>();
        Square position = builder.getPosition();

        int i, j;
        for(i = -1; i <= 1; i++){

            for(j = -1; j <= 1; j++){

                if(i!=0 || j!=0) {
                    int a = position.x + i;
                    int b = position.x + j;
                    if (a >= 0 && a <= 5 && b >= 0 && b <= 5) {
                        int newSquareValue = board.fullMap[a][b].getValue();
                        if (newSquareValue == 0) {
                            Square square = board.fullMap[a][b];
                            possibleBuilds.add(square);
                        }
                    }
                }
            }
        }

        return possibleBuilds;

    }

}











