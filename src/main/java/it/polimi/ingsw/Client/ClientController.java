package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.NetworkHandler.NetInterface;
import it.polimi.ingsw.Parser.Sender;
import it.polimi.ingsw.Client.View.CLI.CliBoard;
import it.polimi.ingsw.Client.View.ClientBoard;
import it.polimi.ingsw.Client.View.Pawn;
import it.polimi.ingsw.Server.Model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * represents the controller in the client side
 */
public class ClientController {

    /** represents the netInterface on the client side linked to the controller */
    private NetInterface netInterface = new NetInterface(this);

    /** represent the client to which the controller is linked */
    private Client client;

    /** represent the boolean that tells if the client is still in the game or not */
    private Boolean stillPlaying;

    /** represents the boolean that tells if the client is in the setup phase or not */
    public Boolean setup;

    /** represents the possible cards available to the player */
    public ArrayList<Card> possibleCards = new ArrayList<Card>();

    /** represents the number of players in the game */
    private Integer numberOfPlayers;

    /** represents the board on the client side */
    private ClientBoard clientBoard = new ClientBoard();

    /** represents the square on which the builder is placed */
    private Square builderSquare;

    /** represents all the possible moves available to a builder */
    private ArrayList<Square> possibleMoves;

    /** represents the boolean that tells if the player is building a dome */
    private boolean dome;


    private HashMap<Integer,String > messages;

    /**
     * create and initialize the client controller
     */
    public ClientController(Client client) {
        this.client = client;
        stillPlaying = true;
        messageMap();
    }

    private void messageMap() {
        messages = new HashMap<>();

        messages.put(0,"");

    }

    /**
     * this method loops until all the players are ready to play
     * @param socket is the socket from where the clients gets the information for the setup
     */
   public void matchSetup(Socket socket) throws IOException {
        setup = true;
        while(setup && stillPlaying){
            netInterface.getMatchSetup(socket, this);
        }
    }

    /**
     * calls the method to ask the Challenger which cards he want to use for the game and sends them back to the server.
     * throws IOException
     */
    public void dealerChoice(String[] cards) throws IOException {
        printCards(cards);
        System.out.println("Choose "+ this.numberOfPlayers +" cards for the game");
        ArrayList<Integer> chosenCards = this.chosenCards();
        netInterface.sendCard(chosenCards, client.getServerSocket());
    }

    /**
     * calls the method to ask the player which card he wants to choose from the cards chosen by the Challenger.
     * throws IOException
     */
    public void playerChoice(String[] cards) throws IOException {
        printCards(cards);
        System.out.println("Pick a card for the game");
        Integer card = this.pickACard();
        if (card < 1 || card > cards.length){
            System.out.println("Select a card with available number");
            playerChoice(cards);
        }
        else {
            netInterface.sendCard(card, client.getServerSocket());
        }

    }

    /**
     * prints all the card available to the player
     * @param cards is the list of all the cards
     */
    public void printCards(String[] cards){
        Card card;
        for (int i = 0; i < cards.length; i++) {
            card = netInterface.stringToCard(cards[i]);
            card.setNumber(i+1);
            possibleCards.add(card);
            System.out.println(card.getNumber() + "\n" + card.getName() + "\n" + card.getDescription() + "\n");
        }
    }

    /**
     * starts the game phase for the client and keeps spinning until the end of the game.
     * @param socket is the socket used to talk with the server.
     */
    public void play(Socket socket) throws IOException {
       // stillPlaying=true;
        Moves moves = new Moves(null, null, null, null, false, false);
        moves.setUpdate(true);
        while(stillPlaying){
            Moves envelope;
            if(!moves.getUpdate()){
                if (moves.getSkippable()){
                    System.out.println("Do you want to perform special card effect? y/n");
                    if (!returnBoolean()){
                        Sender.send("0",client.getServerSocket());
                    }
                    else {
                        envelope = chooseMove(moves);
                        netInterface.sendMoves(envelope, client.getServerSocket());
                    }
                }
                else if(moves.getFemale()){
                    envelope = getSpecialBuild(moves);
                    netInterface.sendMoves(envelope, client.getServerSocket());
                }
                else {
                    envelope = chooseMove(moves);
                    netInterface.sendMoves(envelope, client.getServerSocket());
                }
            }
            moves = netInterface.getMoves(socket);
        }
    }

    /**
     * special method for selene card effect
     * @param moves contains the ArrayList of all the possible moves
     * @return the move made by the player with selene
     * @throws IOException exception for getPosition method
     */
    public Moves getSpecialBuild(Moves moves) throws IOException {
        highlightSquares(moves);

        if (moves.getMoves2() != null){
            System.out.println("Pick a worker: position (x,y)");

            builderSquare = getPosition();
            possibleMoves = chosenBuilder(moves,builderSquare);

            while (possibleMoves == null){
                System.out.println("Invalid pick, chose more wisely");
                builderSquare = getPosition();
                possibleMoves = chosenBuilder(moves,builderSquare);

            }
            CliBoard.drawBoard(clientBoard);

            this.resetBoard();

        }else {
            builderSquare = moves.getBuilder1().getPosition();
            possibleMoves = chosenBuilder(moves,builderSquare);

            CliBoard.drawBoard(clientBoard);

            this.resetBoard();
        }

        System.out.println("Select move: (x,y)");

        Square chosen = getPosition();

        while(!searchArray(possibleMoves,chosen)){
            System.out.println("Invalid pick, chose more wisely");
            chosen = getPosition();
        }

        ArrayList<Square> selected = new ArrayList<Square>();
        selected.add(chosen);

        if (returnBuilder(builderSquare,moves).getSex().equals("Female")){
            System.out.println("Do you want to build a Dome? y/n");
            dome = returnBoolean();
        }

        return new Moves(returnBuilder(builderSquare,moves),selected,null,null,dome,false);
    }

    /**
     * shows to the player all the possible moves at the beginning of the turn by both pawns
     * @param moves contains the ArrayList of the possible moves for both pawns
     */
    public void highlightSquares(Moves moves) {
        if(moves.getMoves1() != null){
        for (int i=0; i<moves.getMoves1().size(); i++){
            for (int j=0; j<5; j++){
                for (int k=0; k<5; k++){
                    if (clientBoard.getCell(j, k).getX() == moves.getMoves1().get(i).x && clientBoard.getCell(j, k).getY() == moves.getMoves1().get(i).y){
                        clientBoard.getCell(j, k).setColour(1);
                    }
                }
            }
        }
    }
        if(moves.getMoves2() != null){
            for (int i=0; i<moves.getMoves2().size(); i++){
                for (int j=0; j<5; j++){
                    for (int k=0; k<5; k++){
                        if (clientBoard.getCell(j, k).getX() == moves.getMoves2().get(i).x && clientBoard.getCell(j, k).getY() == moves.getMoves2().get(i).y) {
                            clientBoard.getCell(j, k).setColour(1);
                        }
                    }
                }
            }
        }
        CliBoard.drawBoard(clientBoard);
        this.resetBoard();
    }

    /**
     * Shows on the screen all the possible moves that the player can do and waits for the choice.
     * @param moves all the moves that the player can do.
     * @return the single move chosen by the player with the builder that is going to do that move.
     */
    public Moves chooseMove(Moves moves) throws IOException {
        highlightSquares(moves);

        if (moves.getMoves2() != null){
            System.out.println("Pick a worker: position (x,y)");

            builderSquare = getPosition();
            possibleMoves = chosenBuilder(moves,builderSquare);

            while (possibleMoves == null){
                System.out.println("Invalid pick, chose more wisely");
                builderSquare = getPosition();
                possibleMoves = chosenBuilder(moves,builderSquare);

            }
            CliBoard.drawBoard(clientBoard);

            this.resetBoard();

        }else {
            builderSquare = moves.getBuilder1().getPosition();
            possibleMoves = chosenBuilder(moves,builderSquare);

            CliBoard.drawBoard(clientBoard);

            this.resetBoard();
        }


        System.out.println("Select move: (x,y)");
        if(moves.getIsDome()){
            System.out.println("You can build a dome by your god's card power");
        }

        Square chosen = getPosition();

        while(!searchArray(possibleMoves,chosen)){
            System.out.println("Invalid pick, chose more wisely");
            chosen = getPosition();
        }

        ArrayList<Square> selected = new ArrayList<Square>();
        selected.add(chosen);

        if(moves.getIsDome()){
            System.out.println("Do you want to build a Dome? y/n");
            dome = returnBoolean();
        }
        else {
            dome = false;
        }

        return new Moves(returnBuilder(builderSquare,moves),selected,null,null,dome,false);
    }

    /**
     * prints that the player has lost
     * sets the stillPlaying flag to false to end the game.
     */
    public void lost(String winnerID){
        this.stillPlaying=false;
        System.out.println("Sorry, you lost.");
        if (!(winnerID.equals("null"))){
            System.out.println(winnerID + " has won");
        }
    }

    /**
     * prints that the player has been disconnected
     * sets the stillPlaying flag to false to end the game.
     */
    public void disconnected(){
        this.stillPlaying=false;
        System.out.println("You have been disconnected");
    }

    /**
     * prints that the player has won
     * sets the stillPlaying flag to false to end the game.
     */
    public void win(){
        this.stillPlaying=false;
        System.out.println("Congratulations! You won!");
    }

    /**
     * sets the number of players of the game locally.
     * @param numberOfPlayers is the number of players in game.
     */
    public void setNumberOfPlayers(Integer numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        System.out.println("Number of Players in game: " + numberOfPlayers);
    }

    /**
     * shows on screen all the 15 cards and waits for the Challenger choice.
     * @return the 2 or 3 chosen cards.
     * @throws IOException
     */
    public ArrayList<Integer> chosenCards() throws IOException {
        ArrayList<Integer> chosenCards = new ArrayList<Integer>();
        for (int i = 0; i<numberOfPlayers; i++){
            int card = pickACard();
            if(i == 0 && card>0 && card<15){
                chosenCards.add(card);
            }
            else if(checkCard(chosenCards,card) && card>0 && card<15){
                chosenCards.add(card);
            }
            else{
                System.out.println("Please choose an available card");
                i--;
            }
        }
        return chosenCards;
    }

    /**
     * checks that the card chosen isn't already been chosen by another player
     * @param choosenCards are all the cards chosen by the other players
     * @param pick is the number of the card chosen by the current player
     * @return the boolean false if the card is already chosen, false otherwise
     */
    public boolean checkCard(ArrayList<Integer> choosenCards, int pick){
        for(int i= 0; i<choosenCards.size(); i++){
            if (choosenCards.get(i) == pick){
                return false;
            }
        }
        return true;
    }

    /**
     * Shows on screen the 2 or 3 cards chosen by the Challenger (or the remaining ones) and waits for the player choice.
     * @return The chosen card.
     * @throws IOException
     */
    public Integer pickACard() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int godNumber = -1;

        while (godNumber == -1){

            try {
                godNumber = Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e){
                System.out.println("Please insert a number");
            }
        }

        return godNumber;
    }

    /**
     * asks the Challenger to choose who will begin the match.
     * @param players is an ArrayList that contains the playerIDs.
     * @throws IOException exception
     */
    public void chooseBeginner(ArrayList<String> players) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int player = 0;
        while (player < 1 || player > players.size()){
            try{
                System.out.println("Insert the number");
                player = Integer.parseInt(reader.readLine());

            }catch (NumberFormatException e){
                System.out.println("Please insert a number");
            }
        }
        String chosenOne = players.get(player-1);
        netInterface.sendFirstPlayer(chosenOne,client.getServerSocket());
    }

    /**
     * shows to the dealer of the players
     * @param names are the players names
     * @throws IOException exception for chooseBeginner method
     */
    public void printPlayersInGame(String[] names) throws IOException {
        ArrayList<String> playerIDs = new ArrayList<String>();
        System.out.println("Pick first player");
        int j;
        for(int i=0; i<names.length; i++){
            j=i+1;
            playerIDs.add(names[i]);
            System.out.println(j + " for " + names[i] + "\n");
        }
        chooseBeginner(playerIDs);
    }

    /**
     *
     * @param freeSquares an ArrayList that contains the available squares to place the worker.
     * @param number is the id of the builder.
     * @throws IOException exception.
     */
    public void placeBuilder(ArrayList<Square> freeSquares, int number) throws IOException {
        System.out.println("Place your builders: positions (x,y) between 1 and 5");
        Square square = getPosition();
        if(checkSquare(freeSquares, square)){
            netInterface.sendSquare(square,client.getServerSocket());
        }
       else{
           System.out.println("please Insert available square.");
           placeBuilder(freeSquares, number);
        }
    }

    /**
     * checks that the move is possible
     * @param freeSquares is the ArrayList of all the unoccupied squares
     * @param square is the square chosen by the player
     * @return boolean true if the move is possible, false otherwise
     */
    public boolean checkSquare(ArrayList<Square> freeSquares, Square square)  {
        for (int i=0; i<freeSquares.size(); i++){
            if( square.x == freeSquares.get(i).x && square.y == freeSquares.get(i).y){
               return true;
            }
        }
        return false;
    }

    /**
     * updates the board for all the players when two squares are changed
     * @param firstSquare is the first changed square
     * @param secondSquare is the second changed square
     */
    public void updateBoard(Square firstSquare, Square secondSquare) {
        for (int i = 0; i < 5; i++){
            for (int j = 0; j < 5; j++){
                if (clientBoard.getCell(i, j).getX() == firstSquare.x && clientBoard.getCell(i, j).getY() == firstSquare.y) {
                    clientBoard.getCell(i, j).setValue(firstSquare.getValue());
                    clientBoard.getCell(i, j).setLevel(firstSquare.getLevel());
                    if (firstSquare.getValue() == 1) {
                        Pawn pawn1 = new Pawn(firstSquare.getBuilder().getColour(), firstSquare.getBuilder().getSex());
                        clientBoard.getCell(i, j).setPawn(pawn1);
                    }


                }
                if (clientBoard.getCell(i, j).getX() == secondSquare.x && clientBoard.getCell(i, j).getY() == secondSquare.y) {
                    clientBoard.getCell(i, j).setValue(secondSquare.getValue());
                    clientBoard.getCell(i, j).setLevel(secondSquare.getLevel());
                    if (secondSquare.getValue() == 1) {
                        Pawn pawn2 = new Pawn(secondSquare.getBuilder().getColour(), secondSquare.getBuilder().getSex());
                        clientBoard.getCell(i, j).setPawn(pawn2);
                    }


                }
            }
        }
        CliBoard.drawBoard(clientBoard);
    }

    /**
     * updates the board for all the players when just a square is changed
     * @param firstSquare the square that has changed
     */
    public void updateBoard(Square firstSquare) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (clientBoard.getCell(i, j).getX() == firstSquare.x && clientBoard.getCell(i, j).getY() == firstSquare.y) {
                    clientBoard.getCell(i, j).setValue(firstSquare.getValue());
                    clientBoard.getCell(i, j).setLevel(firstSquare.getLevel());
                    if (firstSquare.getValue() ==1) {
                        Pawn pawn = new Pawn(firstSquare.getBuilder().getColour(), firstSquare.getBuilder().getSex());
                        clientBoard.getCell(i, j).setPawn(pawn);
                    }
                    else {
                        Pawn pawn = null;
                        clientBoard.getCell(i, j).setPawn(pawn);
                    }
                }
            }
        }
        CliBoard.drawBoard(clientBoard);
    }

    /**
     * clears the client board of the colored possible squares back to white
     */
    private void resetBoard(){
        for (int i=0; i<5; i++){
            for (int j=0; j<5; j++){
                clientBoard.getCell(i,j).setColour(0);
            }
        }
    }

    /**
     * get the player inputs to where place the builders both in placement, move and build phases
     * and checks that the move is within the board limits
     * @return the chosen square to move
     * @throws IOException exception if the input is not formatted correct
     */
    public Square getPosition() throws IOException {
        int x = -1;
        int y = -1;
       while (x<0 || x >4 || y<0 || y>4 ) {

           BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
           String[] inputs = reader.readLine().split(",");
           int[] array = new int[2];

           for (int i = 0; i < array.length; i++) {
               try {
                   array[i] = Integer.parseInt(inputs[i]);
               } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                   System.out.println("Please insert correct format (x,y)");
                   inputs = reader.readLine().split(",");
                   i--;
               }
           }
           x = array[1] - 1;
           y = array[0] - 1;
           if (x<0 || x>4 || y<0 || y>4){
               System.out.println("Please insert number between 1 and 5");
           }
       }
        return new Square(x,y);
    }

    /**
     * highlights the squares of a single pawn
     * @param moves contains the ArrayList of the possible moves for the pawn
     * @param builderSquare is the square where the builder is
     * @return ArrayList of all the possible moves
     */
    public ArrayList<Square> chosenBuilder(Moves moves, Square builderSquare){
        if (moves.getMoves1() != null){
            if (builderSquare.x == moves.getBuilder1().getPosition().x && builderSquare.y == moves.getBuilder1().getPosition().y) {
                for (int i = 0; i < moves.getMoves1().size(); i++) {
                    for (int j = 0; j < 5; j++) {
                        for (int k = 0; k < 5; k++) {
                            if (clientBoard.getCell(j, k).getX() == moves.getMoves1().get(i).x && clientBoard.getCell(j, k).getY() == moves.getMoves1().get(i).y) {
                                clientBoard.getCell(j, k).setColour(1);
                            }
                        }
                    }
                }
                return moves.getMoves1();
            }
        }

        if (moves.getMoves2() != null){
            if (builderSquare.x == moves.getBuilder2().getPosition().x && builderSquare.y == moves.getBuilder2().getPosition().y) {
                for (int i = 0; i < moves.getMoves2().size(); i++) {
                    for (int j = 0; j < 5; j++) {
                        for (int k = 0; k < 5; k++) {
                            if (clientBoard.getCell(j, k).getX() == moves.getMoves2().get(i).x && clientBoard.getCell(j, k).getY() == moves.getMoves2().get(i).y) {
                                clientBoard.getCell(j, k).setColour(1);
                            }
                        }
                    }
                }
                return moves.getMoves2();
            }
        }
        return null;
    }

    /**
     * search in the array if the move is possible to perform
     * @param possibleMoves is the ArrayList of squares of all the possible moves
     * @param chosen is the square chosen to perform the build
     * @return boolean, true if the move is possible, false otherwise
     */
    public boolean searchArray(ArrayList<Square> possibleMoves, Square chosen){
        for(int i=0; i<possibleMoves.size(); i++){
            if(chosen.x == possibleMoves.get(i).x && chosen.y == possibleMoves.get(i).y){
                return true;
            }
        }
        return false;
    }

    /**
     * gives back the builder on the given square
     * @param builderSquare is the square where the builder is placed
     * @param moves are the moves that are possible to perform
     * @return builder on the square if it is on it, null otherwise
     */
    public Builder returnBuilder(Square builderSquare, Moves moves){
        if(moves.getMoves1() != null){
            if(builderSquare.x == moves.getBuilder1().getPosition().x && builderSquare.y == moves.getBuilder1().getPosition().y){
                return moves.getBuilder1();
            }
        }
        if (moves.getMoves2() != null) {
            if (builderSquare.x == moves.getBuilder2().getPosition().x && builderSquare.y == moves.getBuilder2().getPosition().y) {
                return moves.getBuilder2();
            }
        }
        return null;
    }

    /**
     * gives back if the player wants to perform a special move
     * @return the decision via boolean parameter, true if yes, false otherwise
     * @throws IOException exception for the send method
     */
    public boolean returnBoolean() throws IOException{

        String flag;

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        flag = bufferRead.readLine();

        if (flag.equals("y") || flag.equals("yes")){
            return true;
        }
        return false;

    }

    /**
     * asks the dealer which player has to begin the game
     * @return the player number
     * @throws IOException exception if the input format is not correct
     */
    public String askNumberOfPlayers() throws IOException {
        System.out.println("Select number of players\n");

        int number = 0;

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (number <2 || number > 3){
            try {
                System.out.println("Please insert 2 or 3.");
                number = Integer.parseInt(reader.readLine());

            }catch (NumberFormatException e) {
                System.out.println("Please insert a number");
            }

        }

        return Integer.toString(number);
    }

    /**
     *
     */
    public void wrongUserName() {
        System.out.println("Username already taken, please insert another one");
        this.disconnected();
    }
}