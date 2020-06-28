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
    private ArrayList<Square> moves1;
    private ArrayList<Square> moves2;
    private Builder builder1;
    private  Builder builder2;
    private Integer numberOfPlayers;
    private ClientBoard clientBoard = new ClientBoard();

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
    public void dealerChoice() throws IOException {
        System.out.println("Choose "+ this.numberOfPlayers +" cards for the game");
        ArrayList<Integer> chosenCards = this.chosenCards();
        netInterface.sendCard(chosenCards, client.getServerSocket());
    }

    /**
     * calls the method to ask the player which card he wants to choose from the cards chosen by the Challenger.
     * throws IOException
     */
    public void playerChoice() throws IOException {
        System.out.println("Pick a card for the game");
        Integer card = this.pickACard();
        netInterface.sendCard(card, client.getServerSocket());
    }

    /**
     * starts the game phase for the client and keeps spinning until the end of the game.
     * @param socket is the socket used to talk with the server.
     */
    public void play(Socket socket) throws IOException {
        stillPlaying=true;
        while(stillPlaying){
            Moves moves = netInterface.getMoves(socket);
            if(!moves.getUpdate()){
                Moves envelope = chooseMove(moves);
                netInterface.sendMoves(envelope, client.getServerSocket());
            }
        }
        //TODO close the match
        //print message "you have lost"
    }

    /**
     * Shows on the screen all the possible moves that the player can do and waits for the choice.
     * @param moves all the moves that the player can do.
     * @return the single move chosen by the player with the builder that is going to do that move.
     */
    public Moves chooseMove(Moves moves) throws IOException {
        //TODO moves contains all the moves that the player can do, return an envelope with the chosen move
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


        System.out.println("Pick a worker: position (x,y)");

        if (moves.getSkippable()){
            System.out.println("Do you want to perform special card effect? y/n");
            if (!returnSkip()){
                Sender.send("0",client.getServerSocket());
                return null;
            }
        }

        Square builderSquare = getPosition();
        ArrayList<Square> possibleMoves = chosenBuilder(moves,builderSquare);

        while (possibleMoves == null){
            System.out.println("Invalid pick, chose more wisely");
            builderSquare = getPosition();
            possibleMoves = chosenBuilder(moves,builderSquare);

        }

        CliBoard.drawBoard(clientBoard);

        this.resetBoard();

        System.out.println("Select move: (x,y)");

        Square chosen = getPosition();

        while(!searchArray(possibleMoves,chosen)){
            System.out.println("Invalid pick, chose more wisely");
            chosen = getPosition();
        }

        ArrayList<Square> selected = new ArrayList<Square>();
        selected.add(chosen);

        Moves chosenMove = new Moves(returnBuilder(builderSquare,moves),selected,null,null,false,false);

        // se costruisci una cupola per effetto speciale isDome ->true
        //female sempre false

    return  chosenMove;
    }

    /**
     * sets the stillPlaying flag to false to end the game.
     */
    public void lost(){
        this.stillPlaying=false;
    }

    /**
     * sets the number of players of the game locally.
     * @param numberOfPlayers is the number of players in game.
     */
    public void setNumberOfPlayers(Integer numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    /**
     * shows on screen all the 15 cards and waits for the Challenger choice.
     * @return the 2 or 3 chosen cards.
     * @throws IOException
     */
    public ArrayList<Integer> chosenCards() throws IOException {
        ArrayList<Integer> chosenCards = new ArrayList<Integer>();
        for (int i = 0; i<numberOfPlayers; i++){
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            int card = Integer.parseInt(reader.readLine());
            chosenCards.add(i,card);
        }
        return chosenCards;
    }

    /**
     * Shows on screen the 2 or 3 cards chosen by the Challenger (or the remaining ones) and waits for the player choice.
     * @return The chosen card.
     * @throws IOException
     */
    public Integer pickACard() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Integer card = Integer.parseInt(reader.readLine());
        return card;
    }

    /**
     * asks the Challenger to choose
     * @param players
     * @throws IOException
     */
    public void chooseBeginner(ArrayList<String> players) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int player = Integer.parseInt(reader.readLine());
        String chosenOne = players.get(player-1);
        netInterface.sendFirstPlayer(chosenOne,client.getServerSocket());
    }

    /**
     *
     * @param freeSquares
     * @param number
     * @throws IOException
     */
    public void placeBuilder(ArrayList<Square> freeSquares, int number) throws IOException {
        System.out.println("Place your builders: positions (x,y)");
        Square square1 = getPosition();
        for (int i=0; i<freeSquares.size(); i++){
            int tempX = freeSquares.get(i).x;
            int tempY = freeSquares.get(i).y;
            if( square1.x == tempX && square1.y == tempY){
                Square square2 = freeSquares.get(i);
                netInterface.sendSquare(square2,client.getServerSocket());
            }
        }
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
                    Pawn pawn = new Pawn(firstSquare.getBuilder().getColour(),firstSquare.getBuilder().getSex());
                    clientBoard.getCell(i, j).setPawn(pawn);
                    CliBoard.drawBoard(clientBoard);
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
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String[] inputs = reader.readLine().split(",");
        int[] array = new int[inputs.length];
        for (int i=0; i<array.length; i++){
            array[i] = Integer.parseInt(inputs[i]);
        }
        int x = array[1]-1;
        int y = array[0]-1;
        return new Square(x, y);
    }

    public ArrayList<Square> chosenBuilder(Moves moves, Square builderSquare){

        if (builderSquare.x == moves.getBuilder1().getPosition().x && builderSquare.y == moves.getBuilder1().getPosition().y){
            for (int i=0; i<moves.getMoves1().size(); i++){
                for (int j=0; j<5; j++){
                    for (int k=0; k<5; k++){
                        if (clientBoard.getCell(j, k).getX() == moves.getMoves1().get(i).x && clientBoard.getCell(j, k).getY() == moves.getMoves1().get(i).y){
                            clientBoard.getCell(j, k).setColour(1);
                        }
                    }
                }
            }
            return moves.getMoves1();
        }

        if (builderSquare.x == moves.getBuilder2().getPosition().x && builderSquare.y == moves.getBuilder2().getPosition().y){
            for (int i=0; i<moves.getMoves2().size(); i++){
                for (int j=0; j<5; j++){
                    for (int k=0; k<5; k++){
                        if (clientBoard.getCell(j, k).getX() == moves.getMoves2().get(i).x && clientBoard.getCell(j, k).getY() == moves.getMoves2().get(i).y){
                            clientBoard.getCell(j, k).setColour(1);
                        }
                    }
                }
            }
            return moves.getMoves2();
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

    public boolean returnSkip() throws IOException{

        String flag;

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        flag = bufferRead.readLine();

        if (flag.equals("y") || flag.equals("yes")){
            return true;
        }
        return false;

    }

}