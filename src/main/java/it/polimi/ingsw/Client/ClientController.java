package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.NetworkHandler.NetInterface;
import it.polimi.ingsw.Client.NetworkHandler.Sender;
import it.polimi.ingsw.Client.View.CLI.CliBoard;
import it.polimi.ingsw.Client.View.ClientBoard;
import it.polimi.ingsw.Client.View.Pawn;
import it.polimi.ingsw.Server.Model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;


public class ClientController {
    private NetInterface netInterface = new NetInterface(this);
    private Client client;
    private Boolean stillPlaying;
    public Boolean setup;
    public ArrayList<Card> possibleCards = new ArrayList<Card>();
    private Integer numberOfPlayers;
    private ClientBoard clientBoard = new ClientBoard();
    private Square builderSquare;
    private ArrayList<Square> possibleMoves;
    private boolean dome;

    public ClientController(Client client) {
        this.client = client;
    }

    /**
     * this method loops until all the players are ready to play
     * @param socket is the socket from where the clients gets the information for the setup
     */
   public void matchSetup(Socket socket) throws IOException {
        setup = true;
        while(setup){
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
        stillPlaying=true;
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
                else {
                    envelope = chooseMove(moves);
                    netInterface.sendMoves(envelope, client.getServerSocket());
                }
            }
            moves = netInterface.getMoves(socket);
        }
    }

    /**
     * Shows on the screen all the possible moves that the player can do and waits for the choice.
     * @param moves all the moves that the player can do.
     * @return the single move chosen by the player with the builder that is going to do that move.
     */
    public Moves chooseMove(Moves moves) throws IOException {
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
     * sets the stillPlaying flag to false to end the game.
     */
    public void lost(){
        this.stillPlaying=false;
        System.out.println("Sorry, you lost.");
    }

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

    public boolean checkSquare(ArrayList<Square> freeSquares, Square square) throws IOException {
        for (int i=0; i<freeSquares.size(); i++){
            if( square.x == freeSquares.get(i).x && square.y == freeSquares.get(i).y){
               return true;
            }
        }
        return false;
    }

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

    public void updateBoard(Square firstSquare) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (clientBoard.getCell(i, j).getX() == firstSquare.x && clientBoard.getCell(i, j).getY() == firstSquare.y) {
                    clientBoard.getCell(i, j).setValue(firstSquare.getValue());
                    clientBoard.getCell(i, j).setLevel(firstSquare.getLevel());
                    if (firstSquare.getValue() ==1) {
                        Pawn pawn = new Pawn(firstSquare.getBuilder().getColour(), firstSquare.getBuilder().getSex());
                        clientBoard.getCell(i, j).setPawn(pawn);
                        CliBoard.drawBoard(clientBoard);
                    }
                }
            }
        }
    }

    private void resetBoard(){
        for (int i=0; i<5; i++){
            for (int j=0; j<5; j++){
                clientBoard.getCell(i,j).setColour(0);
            }
        }
    }

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
               } catch (NumberFormatException e) {
                   System.out.println("Please insert correct format (x,y)");
                   inputs = reader.readLine().split(",");
                   i--;
               }
               catch (ArrayIndexOutOfBoundsException ex){
                   System.out.println("Please insert correct format (x,y)");
                   inputs = reader.readLine().split(",");
                   i--;
               }
           }
           x = array[1] - 1;
           y = array[0] - 1;
       }
        return new Square(x,y);
    }

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

    public boolean searchArray(ArrayList<Square> possibleMoves, Square chosen){
        for(int i=0; i<possibleMoves.size(); i++){
            if(chosen.x == possibleMoves.get(i).x && chosen.y == possibleMoves.get(i).y){
                return true;
            }
        }
        return false;
    }

    public Builder returnBuilder(Square builderSquare, Moves moves){
        if(builderSquare.x == moves.getBuilder1().getPosition().x && builderSquare.y == moves.getBuilder1().getPosition().y){
            return moves.getBuilder1();
        }
        if (builderSquare.x == moves.getBuilder2().getPosition().x && builderSquare.y == moves.getBuilder2().getPosition().y){
            return moves.getBuilder2();
        }
        else
            return null;

    }

    public boolean returnBoolean() throws IOException{

        String flag;

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        flag = bufferRead.readLine();

        if (flag.equals("y") || flag.equals("yes")){
            return true;
        }
        return false;

    }

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

}