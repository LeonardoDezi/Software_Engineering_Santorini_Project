package it.polimi.ingsw.Server.VirtualView;

import it.polimi.ingsw.Server.Client;
import it.polimi.ingsw.Server.Model.*;
import java.net.Socket;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class NetInterface {
    private ArrayList<Client> clients;
    private final Game game;
    private Player currentPlayer;
    private final Sender sender = new Sender();
    private final Reciever reciever = new Reciever();

    public NetInterface(Game game){
        this.game = game;
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
    public Envelope getBothMovementMove(ArrayList<Square> moves1, Builder builder1,ArrayList<Square> moves2, Builder builder2, Player player){
        this.currentPlayer=player;
        Client client = getClient(player);
        Socket socket=client.getSocket();
        String message = "1@ " + arrayListSquareToString(moves1) + builderToString(builder1) + arrayListSquareToString(moves2) + builderToString(builder2);
        sender.send(message, socket);
        message=reciever.recieve(socket);
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
    public Envelope getMovementMove(ArrayList<Square> moves, Builder builder, Player player){
        Client client = getClient(player);
        Socket socket=client.getSocket();
        String message = "2@ " + arrayListSquareToString(moves) + builderToString(builder);
        //TODO send to the player
        sender.send(message, socket);
        message=reciever.recieve(socket);
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
    public Envelope getBothBuildMove(ArrayList<Square> moves1, Builder builder1, ArrayList<Square> moves2, Builder female, Boolean canBuildADome, Player player){
        Client client = getClient(player);
        Socket socket=client.getSocket();
        String message ="4@ " + arrayListSquareToString(moves1) + builderToString(builder1) + arrayListSquareToString(moves2) + builderToString(female) + wantsToBuildADome(canBuildADome);
        //TODO send to the player
        sender.send(message, socket);
        message=reciever.recieve(socket);
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
    public Envelope getBuildMove(ArrayList<Square>moves, Builder builder, Boolean isDome, Player player){
        Client client = getClient(player);
        Socket socket=client.getSocket();
        String message = "3@ " + arrayListSquareToString(moves) + builderToString(builder) + wantsToBuildADome(isDome);
        if(isDome){
            //sendMessage("vuoi costruire la cupola?", client);
        }
        sender.send(message, socket);
        message=reciever.recieve(socket);
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

    public void sendMessage(int x, String phase, Client client){ //TODO implement the method with the message hashmap
        Socket socket;
        if(client == null){
            for(int i=0; i<game.numberOfPlayers;i++){

            }
        }
        else{
            socket=client.getSocket();

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
        return square.x + "," + square.y;
    }

    /**
     * converts an arrayList of Squares to a string, is used to help serialize the information to send to the client
     * @param moves is the arrayList of Squares
     * @return a string with the coordinates of all the squares of the arrayList
     * the x and y of the same Square are separated by ","
     * two different squares are separated by ":"
     * the string ends with @
     */
    public String arrayListSquareToString(ArrayList<Square> moves){
        if(moves.isEmpty()){
            return null;
        }
        StringBuilder stringMoves= null;
        String partial;
        for (Square move : moves) {
            partial = squareToString(move);
            stringMoves.append(" : ").append(partial);
        }
        stringMoves.append("@ ");
        return stringMoves.toString();
    }

    /**
     * converts a builder object to a string containing the coordinates of the position on the board.
     * @param builder is the builder that is needed to have converted.
     * @return a string containing the builder.
     */
    public String builderToString(Builder builder){
        Square position = builder.getPosition();
        return squareToString(position) + "@ ";
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
     * converts a string containing coordinates x and y separated by "," to a Square with x and y coordinates.
     * @param string is the string that contains the coordinates
     * @return a Square with x and y coordinates and all the other values set to 0.
     */
    public Square stringToSquare(String string){
        String[] coordinates = string.split(",");
        int x;
        int y;
        x = parseInt(coordinates[0]);
        y = parseInt(coordinates[1]);
        return new Square(x, y);
    }

    /**
     * converts back a string with the coordinates of the position of a builder to a builder object usable by the controller.
     * @param string the string with the coordinates x and y separated by ",".
     * @return a builder object.
     */
    public Builder stringToBuilder(String string){
        Square square = stringToSquare(string);
        return new Builder(square, currentPlayer.colour, null);
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


}
