package it.polimi.ingsw.Server.VirtualView;

import it.polimi.ingsw.Parser.Receiver;
import it.polimi.ingsw.Parser.Sender;
import it.polimi.ingsw.Server.Client;
import it.polimi.ingsw.Server.Model.*;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class NetInterface {
    private ArrayList<Client> clients;
    private Game game;
    private Boolean alreadyExecuted;
    private Player currentPlayer;
    private Integer numberOfPlayers;

    public NetInterface(){
        alreadyExecuted = Boolean.FALSE;
        clients = new ArrayList<Client>();
    }

    public void setGame(Game game){
        this.game=game;
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
    public Envelope getBothMovementMove(ArrayList<Square> moves1, Builder builder1,ArrayList<Square> moves2, Builder builder2, Player player, boolean skippable) throws IOException {
        this.currentPlayer=player;
        Client client = getClient(player);
        Socket socket=client.getSocket();
        String message = "1@" + arrayListSquareToString(moves1) + builderToString(builder1) + skippable + "@" + arrayListSquareToString(moves2) + builderToString(builder2);
        Sender.send(message, socket);
        message= Receiver.receive(socket);
        try{
            Integer x = parseInt(message);
            if(x == 0){
                return null;
            }
        }
        catch (NumberFormatException e){
        }
        String[] choosenmove=message.split("@");
        if (choosenmove[0].equals("-1")) {
            return new Envelope(builder1,new Square(20,20),game);
        }
        Square chosenSquare = stringToSquare(choosenmove[0]);
        Builder chosenBuilder = stringToBuilder(choosenmove[1]);
        boolean choice = stringToBool(choosenmove[2]);
        Envelope result = new Envelope(chosenBuilder, chosenSquare,game);
        result.setIsDome(choice);
        return result;
    }

    /**
     * used to ask for a second move after the first one (on the special phases). sends the player the new available moves and waits for the response.
     * if the player chooses not to use a special moves a null answer is expected.
     * @param moves is the new possible moves.
     * @param builder is the builder that is able to move again
     * @param player is the player that has to choose.
     * @return the move chosen by the player or null if the player choses not to take a special action.
     */
    public Envelope getMovementMove(ArrayList<Square> moves, Builder builder, Player player, boolean skippable) throws IOException {
        Client client = getClient(player);
        Socket socket=client.getSocket();
        String message = "2@" + arrayListSquareToString(moves) + builderToString(builder) + skippable;
        Sender.send(message, socket);
        message= Receiver.receive(socket);
        try{
            Integer x = parseInt(message);
            if(x == 0){
                return null;
            }
        }
        catch (NumberFormatException e){
        }
        String[] choosenmove=message.split("@");
        if (choosenmove[0].equals("-1")) {
            return new Envelope(builder,new Square(20,20),game);
        }
        Square chosenSquare = stringToSquare(choosenmove[0]);
        Builder chosenBuilder = stringToBuilder(choosenmove[1]);
        return new Envelope(chosenBuilder, chosenSquare, game);
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
    public Envelope getBothBuildMove(ArrayList<Square> moves1, Builder builder1, ArrayList<Square> moves2, Builder female, Boolean canBuildADome, Player player, boolean skippable) throws IOException {
        Client client = getClient(player);
        Socket socket=client.getSocket();
        String message ="4@" + arrayListSquareToString(moves1) + builderToString(builder1) + skippable + "@" + arrayListSquareToString(moves2) + builderToString(female) + wantsToBuildADome(canBuildADome);
        Sender.send(message, socket);
        message= Receiver.receive(socket);
        try{
            Integer x = parseInt(message);
            if(x == 0){
                return null;
            }
        }
        catch (NumberFormatException e){
        }
        String[] choosenmove = message.split("@");
        if (choosenmove[0].equals("-1")) {
            return new Envelope(builder1,new Square(20,20),game);
        }
        Square chosenSquare = stringToSquare(choosenmove[0]);
        Builder chosenBuilder = stringToBuilder(choosenmove[1]);
        Boolean femaleDome = stringToBool(choosenmove[2]);
        Envelope envelope = new Envelope(chosenBuilder, chosenSquare, game);
        envelope.setIsDome(femaleDome);
        return envelope;

    }

    /**
     * asks the player where does he want to build this turn
     * @param moves is an ArrayList of Squares containig all the places where the player can build.
     * @param builder is the builder that is going to build.
     * @param isDome is a Boolean that if is true signalizes that the player can also build a dome.
     * @param player is the player that has to choose where to build
     * @return and Envelope Object containing where the player wants to build and if he wants to build a dome.
     * if the player has the opportunity to choose to build or not the method can return null if the choice is to not build anything.
     */
    public Envelope getBuildMove(ArrayList<Square>moves, Builder builder, Boolean isDome, Player player, boolean skippable) throws IOException {
        Client client = getClient(player);
        Socket socket=client.getSocket();
        String message = "3@" + arrayListSquareToString(moves) + builderToString(builder) + wantsToBuildADome(isDome) + skippable;
        if(isDome){
            //sendMessage("vuoi costruire la cupola?", client);
        }
        Sender.send(message, socket);
        message= Receiver.receive(socket);
        try{
            Integer x = parseInt(message);
            if(x == 0){
                return null;
            }
        }
        catch (NumberFormatException e){
        }
        String[] choosenmove=message.split("@");
        if (choosenmove[0].equals("-1")) {
            return new Envelope(builder,new Square(20,20),game);
        }
        Square chosenSquare = stringToSquare(choosenmove[0]);
        Builder chosenBuilder = stringToBuilder(choosenmove[1]);
        Boolean hasChosenADome = stringToBool(choosenmove[2]);
        Envelope envelope = new Envelope(chosenBuilder, chosenSquare, game);
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
        String[] received = message.toString().split("@");
        String mex = received[0];
        if(mex.equals("-1")){
            ArrayList<Integer> endMessage = new ArrayList<Integer>();
            endMessage.add(0,-1);
            return endMessage;
        }
        String[] response = mex.split(",");
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
        for (int i=0; i<clients.size(); i++) {
            if (clientID == clients.get(i).clientID) {
                socket = clients.get(i).getSocket();
            }
        }
        if(socket==null){
            System.out.print("Attention! Client not found!");
            return 0;
        }
        StringBuilder partial = new StringBuilder("8@");
        String stringCard;
        for (Card value : possibleCards) {
            stringCard = cardToString(value);
            partial.append(stringCard);
        }
        String message = partial.toString();
        Sender.send(message, socket);
        message = Receiver.receive(socket);
        String[] received = message.split("@");
        message = received[0];
        if(message.equals("-1")){
            return -1;
        }
        String[] recievedCard = message.split(",");
        return stringToInt(recievedCard[0]);
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
        String[] received = message.split("@");
        message = received[0];
        if(message.equals("-1")){
            return new Square(20,20);
        }
        return stringToSquare(message);
    }

    public void sendMessage(String playerID, String playerColour, String playerCard) throws IOException {
        Socket socket;
        String message = new String("5@" + playerID + "@" + playerColour + "@" + playerCard);
        for(int i=0; i<game.numberOfPlayers;i++) {
            socket = clients.get(i).getSocket();
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
            return "null@";
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
     * converts a builder object to a string containing the coordinates of the position on the board and its informations.
     * @param builder is the builder that is needed to have converted.
     * @return a string containing the builder.
     */
    public String builderToString(Builder builder){
        if(builder.equals(null)){
            return "null@";
        }
        Square position = builder.getPosition();
        return squareToString(position) + builder.getColour() + ":" + builder.sex + "@";
    }

    /**
     * is used to set the message to signal that the player can also build a dome.
     * @param isDome if is true the player has the possibility to build a dome.
     * @return String:"1" if isDome is tru, String:"0" if the boolean is false.
     */
    public String wantsToBuildADome(Boolean isDome){
        if(isDome){
            return "1@";
        }
        return "0@";
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

    /**
     * send to the clients the updated squares on the board.
     * @param square1 is the first square that has changed.
     * @param square2 is the second square that has changed.
     * @throws IOException from sender and receiver methods.
     */
    public void updateBoard(Square square1, Square square2) throws IOException {
        Socket socket;
        String message = new String("99@");
        message = message + boardSquare(square1);
        if(square2 != null){
            message = message + "2@" + boardSquare(square2);
        }
        else{
            message = message +"1@";
        }
        for(Integer i=0; i < game.numberOfPlayers; i++){
            socket = clients.get(i).getSocket();
            Sender.send(message, socket);
        }
    }

    /**
     * transforms a square to a string.
     * @param square is the chosen square.
     * @return the string that represent che chosen square.
     */
    public String boardSquare(Square square){
        StringBuilder string = new StringBuilder();
        string.append(square.x);
        string.append(",");
        string.append(square.y);
        string.append(":");
        string.append(square.getLevel());
        string.append(",");
        string.append((square.getValue()));
        if(square.getValue()==1){
            string.append("@");
            string.append(square.getBuilder().getColour());
            string.append(",");
            string.append((square.getBuilder().sex));
        }
        else{
            string.append("@1");
        }
        string.append("@");
        String message = string.toString();
        return  message;
    }

    /**
     * asks the challenger the number of players in game.
     * @param client is the challenger.
     * @return the number of players that has been choosen.
     * @throws IOException from sender and receiver methods.
     */
    public Integer getNumberOfPlayers(Client client) throws IOException {
        Sender.send("11@", client.getSocket());
        String recieved = Receiver.receive(client.getSocket());
        String[] numbers = recieved.split("@");
        String numberOfP = numbers[0];
        if(numberOfP.equals("-1")){
            return -1;
        }
        Integer numberOfPlayers = parseInt(numberOfP);
        this.setNumberOfPlayers(numberOfPlayers);
        return numberOfPlayers;
    }

    /**
     * sends the number of players in the game to all clients.
     * @throws IOException from sender.
     */
    public void sendNumber() throws IOException {
        for(int i=0; i<clients.size(); i++){
            Sender.send("12@"+String.valueOf(numberOfPlayers), clients.get(i).getSocket());
        }
    }

    public void setNumberOfPlayers(Integer x){
        this.numberOfPlayers = x;
    }

    /**
     * asks to the challenger which player has to begin the match.
     * @param dealer is the challenger.
     * @param players is the list of all players in game.
     * @return the chosen player.
     * @throws IOException from sender and receiver.
     */
    public Player askFirstPlayer(Client dealer, ArrayList<Player> players) throws IOException {
        StringBuilder partial = new StringBuilder("13@");
        for(int i=0; i< players.size(); i++){
            partial.append(players.get(i).playerID);
            partial.append(":");
        }
        String message = partial.toString();
        Sender.send(message, dealer.getSocket());
        String recieved = Receiver.receive(dealer.getSocket());
        String[] ids = recieved.split("@");
        String firstID = ids[0];
        if(firstID.equals("-1")){
            return new Player("-1","purple",game,-1);
        }
        for(int j=0; j<players.size(); j++){
            if(players.get(j).playerID.equals(firstID)){
                System.out.println("the first Player is " + firstID);
                return players.get(j);
            }
        }
        System.out.println("Player not found, the Dealer begins.");
        return players.get(0);
    }

    /**
     * sends to the player that lost the "lose" message.
     * @param player is the loser.
     * @param winnerID is the winner name.
     * @throws IOException from sender method.
     */
    public void loseMethod(Player player, String winnerID) throws IOException {
        Client loser = findClient(player);
        String loseMessage = new String("66@"+winnerID);
        try {
            Sender.send(loseMessage, loser.getSocket());
        }catch (IOException e){
            System.out.println("Client disconnected");
        }

    }

    /**
     * send to the player that won the "win" message.
     * @param player is the winner.
     * @throws IOException from sender method.
     */
    public void winner(Player player) throws IOException {
        Client winnerClient = findClient(player);
        try{
            Sender.send("73@", winnerClient.getSocket());
        }catch (IOException e){
            System.out.println("Client disconnected");
        }
    }

    /**
     * method used to find the client associated to the player.
     * @param player is the player to search.
     * @return the player's client.
     */
    public Client findClient(Player player) {
        for(int i=0; i< clients.size(); i++){
            if(clients.get(i).clientID == player.clientID){
               return clients.get(i);
            }
        }
            System.out.println("Error, Client non existent <<LoseMethod>>");
        return null;
    }

    public ArrayList<Client> getClients(){
        return clients;
    }
}
