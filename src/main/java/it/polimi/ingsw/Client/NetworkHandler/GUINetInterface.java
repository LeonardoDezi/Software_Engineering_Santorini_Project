package it.polimi.ingsw.Client.NetworkHandler;

import it.polimi.ingsw.Client.GUIClientController;
import it.polimi.ingsw.Client.Moves;
import it.polimi.ingsw.Parser.Receiver;
import it.polimi.ingsw.Parser.Sender;
import it.polimi.ingsw.Server.Model.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.ArrayList;
import static java.lang.Integer.parseInt;

public class GUINetInterface {

    private ArrayList<Square> moves1;
    private ArrayList<Square> moves2;
    private Builder builder1;
    private Builder builder2;
    private GUIClientController clientController;
    private Moves moves = new Moves(builder1, moves1, builder2, moves2, false, false);
    private Square firstSquare;
    private Square secondSquare;


    public GUINetInterface(GUIClientController clientController) {
        this.clientController = clientController;
    }

    /**
     * method used during the game to show the player what possibility has to choose and wait for the choice
     *
     * @param socket is the Server socket.
     * @return the move chosen by the player depending on the phase of the game
     */
    public Moves getMoves(Socket socket) throws IOException, InvocationTargetException, InterruptedException {
        String availableMoves = Receiver.receive(socket);
        String[] values = availableMoves.split("@");

        if (moves.getUpdate()){
            moves.setUpdate(false);
        }
        if (values[0].equals("0")) {
            return null;
        }
        if (values[0].equals("-1")) {                                 //values[0]
            clientController.disconnected();
        }


        if (values[0].equals("1")) {                                  //values[0]

            if(values[1].equals("null")){              //values[1]
                moves.setMoves1(null);
                moves.setBuilder1(null);
            }
            else{
                moves1 = stringToArrayListSquare(values[1]);
                builder1 = stringToBuilder(values[2]);
                moves.setMoves1(moves1);
                moves.setBuilder1(builder1);
            }


            if(values[3].equals("true")){            //values[3]
                moves.setSkippable(true);
            }
            else{
                moves.setSkippable(false);
            }


            if(values[4].equals("null")){             //values[4]
                moves.setMoves2(null);
                moves.setBuilder2(null);
            }
            else{
                moves2 = stringToArrayListSquare(values[4]);
                builder2 = stringToBuilder(values[5]);
                moves.setMoves2(moves2);
                moves.setBuilder2(builder2);
            }


            moves.setIsDome(false);
            moves.setFemale(false);

        }                                                               //values[0]


        if (values[0].equals("2")) {                                    //values[0]
            if(values[1].equals("null")){
                moves.setMoves1(null);
                moves.setBuilder1(null);
            }
            else{
                moves1 = stringToArrayListSquare(values[1]);
                builder1 = stringToBuilder(values[2]);
                moves.setMoves1(moves1);
                moves.setBuilder1(builder1);
            }
            if(values[3].equals("true")){
                moves.setSkippable(true);
            }
            else {
                moves.setSkippable(false);
            }
            moves.setMoves2(null);
            moves.setBuilder2(null);
            moves.setIsDome(false);
            moves.setFemale(false);
        }


        if (values[0].equals("3")) {
            if(values[1].equals("null")){
                moves.setMoves1(null);
                moves.setBuilder1(null);
                moves.setIsDome(false);
            }
            else{
                moves1 = stringToArrayListSquare(values[1]);
                builder1 = stringToBuilder(values[2]);
                moves.setMoves1(moves1);
                moves.setBuilder1(builder1);
                boolean isDome = stringToBool(values[3]);
                moves.setIsDome(isDome);
                if(values[4].equals("true")){
                    moves.setSkippable(true);
                }
                else{
                    moves.setSkippable(false);
                }
            }
            moves.setMoves2(null);
            moves.setBuilder2(null);
            moves.setFemale(false);
        }
        if (values[0].equals("4")) {
            if(values[1].equals("null")){
                moves.setMoves1(null);
                moves.setBuilder1(null);
                moves.setIsDome(false);
            }
            else{
                moves1 = stringToArrayListSquare(values[1]);
                builder1 = stringToBuilder(values[2]);
                moves.setMoves1(moves1);
                moves.setBuilder1(builder1);
                moves.setIsDome(false);
            }
            if(values[3].equals("true")){
                moves.setSkippable(true);
            }
            else{
                moves.setSkippable(false);
            }
            if(values[4].equals("null")){
                moves.setBuilder2(null);
                moves.setMoves2(null);
                moves.setFemale(false);
            }
            else{
                moves2 = stringToArrayListSquare(values[4]);
                builder2 = stringToBuilder(values[5]);
                boolean female = stringToBool(values[6]);
                moves.setMoves2(moves2);
                moves.setBuilder2(builder2);
                moves.setFemale(female);
            }
        }
        if (values[0].equals("5")) { //text message
            String playerID = values[1];
            String playerColour = values[2];
            String playerCard = values[3];
    //        clientController.printMatchInfo(playerID, playerColour, playerCard);
            moves = null;
        }
        if (values[0].equals("66")) { //the client loses
            String winnerID;
            winnerID = values[1];
            clientController.lose(winnerID);
        }
        if(values[0].equals("73")){ //the client wins
            clientController.win();
        }
        if (values[0].equals("99")){
            Builder worker1;
            Builder worker2;
            String[] square = values[1].split(":");
            Square firstSquare = stringToFullSquare(square);


            if(values[2].equals("1")){
                worker1 = null;
                firstSquare.setBuilder(worker1);
            }
            else{
                String[] builderInfo = values[2].split(",");
                worker1 = new Builder(firstSquare, builderInfo[0], builderInfo[1]);
                firstSquare.setBuilder(worker1);
            }

            if(values[3].equals("2")){
                String[] square2 = values[4].split(":");
                Square secondSquare = stringToFullSquare(square2);
                if(values[5].equals("1")){
                    worker2 = null;
                    secondSquare.setBuilder(worker2);
                }
                else{
                    String[] secondBuilderInfo = values[5].split(",");
                    worker2 = new Builder(secondSquare, secondBuilderInfo[0], secondBuilderInfo[1]);
                    secondSquare.setBuilder(worker2);
                }
                moves.setUpdate(true);
                clientController.updateBoard(firstSquare, secondSquare);
            }
            else{
                moves.setUpdate(true);
                clientController.updateBoard(firstSquare);
            }

        }
        return moves;
    }

    /**
     * Converts a String to a Square including the "value" and "level" attributes.
     * @param square2 is a String[] formatted as "x,y:level,value".
     * @return the full Square with the attributes.
     */
    private Square stringToFullSquare(String[] square2) {
        String[] coord2 = square2[0].split(",");
        String[] attributes2 = square2[1].split(",");
        Square square = new Square(parseInt(coord2[0]), parseInt(coord2[1]));
        square.setLevel(parseInt(attributes2[0]));
        square.setValue(parseInt(attributes2[1]));
        return square;
    }

    /**
     * method used to setup the match by getting the cards, the available squares to place the builders and to decide who will be the first player.
     * @param socket is the serverSocket.
     * @param controller is the controller of the player.
     */
    public void getMatchSetup(Socket socket, GUIClientController controller) throws IOException, InterruptedException, InvocationTargetException {
        String availableMoves = Receiver.receive(socket);
        String[] values = availableMoves.split("@");
        if(values[0].equals("-1")){
            clientController.disconnected();
        }
        if (values[0].equals("7")) {//dealer has to choose all the cards
            controller.dealerChoice();
        }
        if (values[0].equals("8")) {//player has to choose the card
            String[] cards = values[1].split(":");
            Card card;
            for (int i = 0; i < cards.length; i++) {
                card = stringToCard(cards[i]);
                card.setNumber(i+1);
                controller.possibleCards.add(card);
            }
            controller.playerChoice();
        }
        if (values[0].equals("9")) {//player has to choose where to place the builder
            String places = values[1];
            Integer builderNumber = parseInt(values[2]);
            ArrayList<Square> freeSquares = stringToArrayListSquare(places);
            controller.placeBuilder(freeSquares, builderNumber);
        }
        if (values[0].equals("10")) { //la partita comincia
            controller.getFrame().setup = false;
            controller.play();
        }
        if (values[0].equals("11")) {
            controller.chooseNumberOfPlayers();
        }
        if (values[0].equals("12")){
            String number = values[1];
            Integer numberOfPlayers = parseInt(number);
            controller.setNumberOfPlayers(numberOfPlayers);
        }

        if (values[0].equals("13")){   //choose the beginner
            String received = values[1];
            String[] names = received.split(":");
            ArrayList<String> playerIDs = new ArrayList<String>();
            int j = 0;
            for(int i=0; i<names.length; i++){
                j=i+1;
                playerIDs.add(names[i]);
                System.out.println(j + "for " + names[i] + "\n");
            }
            clientController.chooseBeginner(playerIDs);
        }
        if (values[0].equals("99")){   //updateboard
            Builder worker1;
            Builder worker2;
            String[] square = values[1].split(":");
            firstSquare = stringToFullSquare(square);
            if(!values[2].equals("1")){
                String[] builderInfo = values[2].split(",");
                worker1 = new Builder(firstSquare, builderInfo[0], builderInfo[1]);
                firstSquare.setBuilder(worker1);
            }
            else{
                worker1 = null;
                firstSquare.setBuilder(worker1);
            }
            if(values[3].equals("2")){
                String[] square2 = values[4].split(":");
                secondSquare = stringToFullSquare(square2);
                if(!values[5].equals("1")){
                    String[] secondBuilderInfo = values[5].split(",");
                    worker2 = new Builder(secondSquare, secondBuilderInfo[0], secondBuilderInfo[1]);
                    secondSquare.setBuilder(worker2);
                }
                else{
                    worker2 = null;
                    secondSquare.setBuilder(worker2);
                }
                clientController.updateBoard(firstSquare, secondSquare);
            }
            else {
                clientController.updateBoard(firstSquare);
            }

        }

    }

    /**
     * sends the cards chosen by the Dealer to the server
     * @param choosenCards is the ArrayList that contains all the cards.
     * @param socket is the server socket.
     */
    public void sendCard(ArrayList<Integer> choosenCards, Socket socket) throws IOException {
        String message = new String();
        for(int i=0; i<choosenCards.size(); i++){
            message = message + Integer.toString(choosenCards.get(i)) + ",";
        }
        Sender.send(message,socket);
    }

    /**
     * sends the card chosen by the player to the server.
     * @param card is the choosen card.
     * @param socket is the server socket.
     */
    public void sendCard(Integer card, Socket socket) throws IOException {
        card = card-1;
        String message = Integer.toString(card);
        Sender.send(message, socket);
    }

    public void sendMoves(Moves envelope, Socket socket) throws IOException {
        if(envelope == null){
            Sender.send("0", socket);
        }
        if (envelope != null) {
            Square square = envelope.getMoves1().get(0);
            Builder builder = envelope.getBuilder1();
            Boolean dome = envelope.getIsDome();
            String coordinates = squareToString(square) + builderToString(builder) + booleanToString(dome);
            Sender.send(coordinates, socket);
        }
    }

    public void sendSquare(Square square, Socket socket) throws IOException {

        String message = squareToString(square);
        Sender.send(message,socket);
    }

    /**
     * converts a builder object to a string containing the coordinates of the position on the board.
     * @param builder is the builder that is needed to have converted.
     * @return a string containing the builder.
     */
    public String builderToString(Builder builder){
        Square position = builder.getPosition();
        return squareToString(position);
    }

    /**
     * converts a Square object to a String containing the coordinates
     * @param square is the square that is going to be converted
     * @return a string with the x and y coordinates of the square separated by ","
     */
    public String squareToString(Square square){
        return square.x + "," + square.y + "@";
    }

    /**
     * converts back a string with the coordinates of the position of a builder to a builder object usable by the controller.
     * @param string the string with the coordinates x and y separated by ",".
     * @return a builder object.
     */
    public Builder stringToBuilder(String string){
        String[] builderInfo = string.split(":");
        Square square = stringToSquare(builderInfo[0]);
        return new Builder(square, builderInfo[1], builderInfo[2]);
    }

    /**
     * reads an integer from a string a converts it to a boolean. '0' is false '1' is true.
     * @param string is the string from where to read
     * @return true if the number in the string is '1', false if it is '0'.
     */
    public Boolean stringToBool(String string){
        int x;
        x=parseInt(string);
        return x == 1;
    }

    /**
     * converts a string containing coordinates x and y separated by "," to a Square with x and y coordinates.
     * @param string is the string that contains the coordinates
     * @return a Square with x and y coordinates and all the other values set to 0.
     */
    public Square stringToSquare(String string){
        StringBuilder partial = new StringBuilder(string);
        if(string.length()>3){
            try{
                partial.delete(3,6);
            }
            catch (StringIndexOutOfBoundsException e){
                partial.delete(3,4);
            }
        }
        string=partial.toString();
        String[] coordinates = string.split(",");
        int x;
        int y;
        x = parseInt(coordinates[0]);
        y = parseInt(coordinates[1]);
        return new Square(x, y);
    }

    /**
     * converts a string containing the coordinates of an indefinite number of squares to an Arraylist with those Squares.
     * @param value is the Arraylist formatted as x and y coordinates separated by "," and Squares separated by ":".
     * @return the ArrayList of Squares.
     */
    public ArrayList<Square> stringToArrayListSquare(String value){
        ArrayList<Square> possiblemoves = new ArrayList<Square>();
        StringBuilder lastRemove = new StringBuilder(value);
        lastRemove.delete(value.length()-1, value.length());
        value = lastRemove.toString();
        String[] squares = value.split(":");
        for(int i=0; i<squares.length; i++){
            Square square = stringToSquare(squares[i]);
            possiblemoves.add(square);
        }
        return possiblemoves;
    }

    /**
     * is used to set the message to signal that the player can also build a dome.
     * @param isDome if is true the player has the possibility to build a dome.
     * @return String:"1" if isDome is tru, String:"0" if the boolean is false.
     */
    public String booleanToString(Boolean isDome){
        if(isDome){
            return "1";
        }
        return "0";
    }

    /**
     * converts a String containing the name and description of a card to a card object.
     * @param string a string containing name and description of a card separated by "_".
     * @return the card object with name and description set.
     */
    public Card stringToCard(String string){
        String[] parts = string.split("_");
        String name = parts[0];
        String description = parts[1];
        Card card = new Card();
        card.name=name;
        card.setDescription(description);
        return card;
    }

    public void sendFirstPlayer(String player, Socket serverSocket) throws IOException {
        Sender.send(player, serverSocket);
    }

}
