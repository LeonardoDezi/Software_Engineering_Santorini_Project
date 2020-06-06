package it.polimi.ingsw.Server.VirtualView;

import it.polimi.ingsw.Server.Client;
import it.polimi.ingsw.Server.Model.*;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import static java.lang.Integer.numberOfLeadingZeros;
import static java.lang.Integer.parseInt;

public class NetInterface {
    private ArrayList<Client> clients;
    private final Game game;
    private Player currentPlayer;
    private final Sender sender = new Sender();
    private final Receiver receiver = new Receiver();

    public NetInterface(Game game){
        this.game = game;
        currentPlayer = new Player("Giocatore", "green", game, 1);
    }

    /**
     * sends to a specific client all the available moves with both builders and waits for the player response on whitch to use.
     * if an arrayList is empty signifies that the corresponding builder cannot move from it's position.
     * @param moves1 ArrayList containing all the available moves for the first builder.
     * @param builder1 the first player builder.
     * @param moves2 ArrayList containing all the available moves for the second builder.
     * @param builder2 the second player builder.
     * @param player the player that has to choose the move.
     * @return the move that the player has choosen.
     */
    public Envelope getBothMovementMove(ArrayList<Square> moves1, Builder builder1,ArrayList<Square> moves2, Builder builder2, Player player) throws IOException {
        this.currentPlayer=player;
        Client client = getClient(player);
        Socket socket=client.getSocket();
        String message = "1@" + arrayListSquareToString(moves1) + builderToString(builder1) + arrayListSquareToString(moves2) + builderToString(builder2);
        Sender.send(message, socket);
        message= Receiver.receive(socket);
        String[] choosenmove=message.split("@");
        Square chosenSquare = stringToSquare(choosenmove[0]);
        Builder chosenBuilder = stringToBuilder(choosenmove[1]);
        return new Envelope(chosenBuilder, chosenSquare);
    }

    /**
     * used to ask for a second move after the first one (on the special phases). sends the player the new available moves and waits for the response.
     * if the player chooses not to use a special moves a null answer is expected.
     * @param moves is the new possible moves.
     * @param builder is the builder that is able to move again
     * @param player is the player that has to choose.
     * @return the move chosen by the player or null if the player choses not to take a special action.
     */
    public Envelope getMovementMove(ArrayList<Square> moves, Builder builder, Player player) throws IOException {
        Client client = getClient(player);
        Socket socket=client.getSocket();
        String message = "2@" + arrayListSquareToString(moves) + builderToString(builder);
        Sender.send(message, socket);
        message= Receiver.receive(socket);
        if(message==null){
            return null;
        }
        String[] choosenmove=message.split("@");
        Square chosenSquare = stringToSquare(choosenmove[0]);
        Builder chosenBuilder = stringToBuilder(choosenmove[1]);
        return new Envelope(chosenBuilder, chosenSquare);
    }

    /**
     * method used to ask the player with whitch builder he wants to build if it is allowed by the card of the player.
     * @param moves1 the places where the first builder can build.
     * @param builder1 the first builder.
     * @param moves2 the places where the second builder can build.
     * @param female the second builder.
     * @param canBuildADome true if the second builder can build a dome.
     * @param player the player that has to choose where to build.
     * @return an Envelope Object with the choice of the player.
     */
    public Envelope getBothBuildMove(ArrayList<Square> moves1, Builder builder1, ArrayList<Square> moves2, Builder female, Boolean canBuildADome, Player player) throws IOException {
        Client client = getClient(player);
        Socket socket=client.getSocket();
        String message ="4@" + arrayListSquareToString(moves1) + builderToString(builder1) + arrayListSquareToString(moves2) + builderToString(female) + wantsToBuildADome(canBuildADome);
        Sender.send(message, socket);
        message= Receiver.receive(socket);
        String[] choosenmove = message.split("@");
        Square chosenSquare = stringToSquare(choosenmove[0]);
        Builder chosenBuilder = stringToBuilder(choosenmove[1]);
        Boolean femaleDome = stringToBool(choosenmove[2]);
        Envelope envelope = new Envelope(chosenBuilder, chosenSquare);
        envelope.setIsDome(femaleDome);
        return envelope;

    } //TODO remember that the second builder is the female so ask the player if he wants to build a dome
      // is possible ONLY for the female one


    /**
     * asks the player where does he want to build this turn
     * @param moves is an ArrayList of Squares containig all the places where the player can build.
     * @param builder is the builder that is going to build.
     * @param isDome is a Boolean that if is true signalizes that the player can also build a dome.
     * @param player is the player that has to choose where to build
     * @return and Envelope Object containing where the player wants to build and if he wants to build a dome.
     * if the player has the opportunity to choose to build or not the method can return null if the choice is to not build anything.
     */
    public Envelope getBuildMove(ArrayList<Square>moves, Builder builder, Boolean isDome, Player player) throws IOException {
        Client client = getClient(player);
        Socket socket=client.getSocket();
        String message = "3@" + arrayListSquareToString(moves) + builderToString(builder) + wantsToBuildADome(isDome);
        if(isDome){
            //sendMessage("vuoi costruire la cupola?", client);
        }
        Sender.send(message, socket);
        message= Receiver.receive(socket);
        if(message==null){
            return null;
        }
        String[] choosenmove=message.split("@");
        Square chosenSquare = stringToSquare(choosenmove[0]);
        Builder chosenBuilder = stringToBuilder(choosenmove[1]);
        Boolean hasChosenADome = stringToBool(choosenmove[2]);
        Envelope envelope = new Envelope(chosenBuilder, chosenSquare);
        envelope.setIsDome(hasChosenADome);
        return envelope;
    }

    /**
     * method use to make the Dealer choose whitch cards will partecipate to the game.
     * @param dealer is the dealer player.
     * @param deck is the Deck containing all the cards.
     * @return an arrayList of integers containing the index number of the choosen cards.
     */
    public ArrayList<Integer> getCards(Client dealer, ArrayList<Card> deck) throws IOException {
        Socket socket = dealer.getSocket();
        StringBuilder message = new StringBuilder("7@");
        String stringCard;
        for (Card value : deck) {
            stringCard = cardToString(value);
            message.append(stringCard);
        }
        Sender.send(message.toString(), socket);
        message = new StringBuilder(Receiver.receive(socket));
        String[] response = message.toString().split(",");
        ArrayList<Integer> choosenCards = new ArrayList<>();
        for (String s : response) {
            int x = parseInt(s);
            choosenCards.add(x);
        }
        return choosenCards;
    }

    /**
     * method used to make a player whitch card he wants to use for the game
     * @param possibleCards the available cards to choose
     * @param clientID the ID of the client whitch has to choose the card
     * @return the number of the card choosen by the player
     */
    public Integer getChosenCard(ArrayList<Card> possibleCards, Integer clientID) throws IOException {
        Socket socket = new Socket();
        for (Client client : clients) {
            if (clientID == client.clientID) {
                socket = client.getSocket();
            }
        }
        if(socket==null){
            System.out.print("Attention! Client not found!");
            return 0;
        }
        StringBuilder partial = new StringBuilder("8@");
        for(int i=0; i<possibleCards.size(); i++){
            partial.append(String.valueOf(possibleCards.get(i).getNumber())).append(", ");
        }
        String message = partial.toString();
        Sender.send(message, socket);
        message = Receiver.receive(socket);
        return stringToInt(message);
    }

    /**
     * method used to ask one player where he wants to place one of its builders at the beginning of the match.
     * @param possibleSquares all the available squares to place the builder.
     * @param clientID the ID of the client whitch has to place the builder.
     * @param buildernumber a number used to distinct from first and second builder.
     * @return the Square where the player wants to place the builder.
     */
    public Square getBuilderPlacement(ArrayList<Square> possibleSquares, int clientID, int buildernumber) throws IOException {
        Socket socket = new Socket();
        for (Client client : clients) {
            if (clientID == client.clientID) {
                socket = client.getSocket();
            }
        }
        if(socket==null){
            System.out.print("Attention! Client not found!");
            return new Square(-1, -1);
        }
        String message = "9@" + arrayListSquareToString(possibleSquares) + buildernumber;
        Sender.send(message, socket);
        message = Receiver.receive(socket);
        return stringToSquare(message);
    }

    public void sendMessage(Integer messageType, Client client) throws IOException { //TODO implement the method with the message hashmap
        Socket socket;
        String message = new String("5@" + messageType.toString());
        if(client == null){
            for(int i=0; i<game.numberOfPlayers;i++){
                socket = clients.get(i).getSocket();
                Sender.send(message, socket);
            }
        }
        else{
            socket=client.getSocket();
            Sender.send(message, socket);
        }
    }

    /**
     * adds a new client to the client list
     * @param client the client to add to the list
     */
    public void addClient(Client client){
        this.clients.add(client);
    }

    /**
     * converts a Square object to a String containing the coordinates
     * @param square is the square that is going to be converted
     * @return a string with the x and y coordinates of the square separated by ","
     */
    public String squareToString(Square square){
        return square.x + "," + square.y + ":";
    }

    /**
     * converts an arrayList of Squares to a string, is used to help serialize the information to send to the client
     * @param moves is the arrayList of Squares
     * @return a string with the coordinates of all the squares of the arrayList
     * the x and y of the same Square are separated by ","
     * two different squares are separated by ":"
     * the string ends with @.
     */
    public String arrayListSquareToString(ArrayList<Square> moves){
        if(moves.isEmpty()){
            return null;
        }
        StringBuilder stringMoves= new StringBuilder();
        String partial;
        for (Square move : moves) {
            partial = squareToString(move);
            stringMoves.append(partial);
        }
        stringMoves.append("@");
        return stringMoves.toString();
    }

    /**
     * converts a builder object to a string containing the coordinates of the position on the board.
     * @param builder is the builder that is needed to have converted.
     * @return a string containing the builder.
     */
    public String builderToString(Builder builder){
        Square position = builder.getPosition();
        return squareToString(position) + "@";
    }

    /**
     * is used to set the message to signal that the player can also build a dome.
     * @param isDome if is true the player has the possibility to build a dome.
     * @return String:"1" if isDome is tru, String:"0" if the boolean is false.
     */
    public String wantsToBuildADome(Boolean isDome){
        if(isDome){
            return "1";
        }
        return "0";
    }



    /**
     * converts back a string with the coordinates of the position of a builder to a builder object usable by the controller.
     * @param string the string with the coordinates x and y separated by ",".
     * @return a builder object.
     */
    public Builder stringToBuilder(String string){
        Square square = stringToSquare(string);
        return new Builder(square, currentPlayer.colour, "not known");
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
     * is used to get the client Object of a specific player
     * @param player the player.
     * @return the client object relative to the player.
     */
    public Client getClient(Player player){
        for (Client client : clients) {
            if (player.clientID == client.clientID) {
                return client;
            }
        }
        return null;
    }


    /**
     * converts a Card object to a string
     * @param card is the card that needs to be serialized
     * @return a string with the name of the card "," the description of the card and ": " as separators
     */
    public String cardToString(Card card){
        String string = card.name + "_" + card.getDescription() + ":";
        return string;
    }

    public void startGame() throws IOException {
        String message = "10@";
        for(int i=0; i < game.numberOfPlayers; i++){
            Sender.send(message, clients.get(i).getSocket());
        }
    }

    /**
     * converts a String containing a number to an integer.
     * @param string is the string containing the number.
     * @return the integer.
     */
    Integer stringToInt(String string){
        int x;
        x = parseInt(string);
        return x;
    }

    /**
     * converts a string containing coordinates x and y separated by "," to a Square with x and y coordinates.
     * @param string is the string that contains the coordinates
     * @return a Square with x and y coordinates and all the other values set to 0.
     */
    public Square stringToSquare(String string){
        StringBuilder partial = new StringBuilder(string);
        partial.delete(3,6);
        string= partial.toString();
        String[] coordinates = string.split(",");
        int x;
        int y;
        x = parseInt(coordinates[0]);
        y = parseInt(coordinates[1]);
        return new Square(x, y);
    }

    public void updateBoard(Square square1, Square square2) throws IOException {
        Socket socket;
        String message = new String("11@");
        message = message + boardSquare(square1);
        if(square2 != null){
            message = message + boardSquare(square2);
        }
        for(Integer i=0; i < game.numberOfPlayers; i++){
            socket = clients.get(i).getSocket();
            sender.send(message, socket);
        }
    }

    public String boardSquare(Square square){
        StringBuilder string = new StringBuilder();
        string.append(square.x);
        string.append(",");
        string.append(square.y);
        string.append(":");
        string.append(square.getLevel());
        string.append(",");
        string.append((square.getValue()));
        string.append("@");
        String message = string.toString();
        return  message;
    }

}
