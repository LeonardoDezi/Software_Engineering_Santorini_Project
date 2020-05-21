package it.polimi.ingsw.Server.VirtualView;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.sun.tools.doclint.Env;
import it.polimi.ingsw.Server.Client;
import it.polimi.ingsw.Server.Model.*;
import sun.nio.ch.Net;

import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class NetInterface {
    private ArrayList<Client> clients;
    private Game game;
    private Player currentPlayer;

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
        String message = arrayListSquareToString(moves1) + builderToString(builder1) + arrayListSquareToString(moves2) + builderToString(builder2);
        //TODO send to the player
        // wait for response
        //message = ;//ricevuto
        String[] choosenmove=message.split("@");
        Square chosenSquare = stringToSquare(choosenmove[0]);
        Builder chosenBuilder = stringToBuilder(choosenmove[1]);
        Envelope envelope = new Envelope(chosenBuilder, chosenSquare);
        return envelope;
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
        String message = arrayListSquareToString(moves) + builderToString(builder);
        //TODO send to the player
        // wait for response
        //message = ;//ricevuto
        if(message==null){
            return null;
        }
        String[] choosenmove=message.split("@");
        Square chosenSquare = stringToSquare(choosenmove[0]);
        Builder chosenBuilder = stringToBuilder(choosenmove[1]);
        Envelope envelope = new Envelope(chosenBuilder, chosenSquare);
        return envelope;
    }

    public Envelope getBothBuildingMove(){

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
    public Envelope getBuildMove(ArrayList<Square>moves, Builder builder, Boolean isDome, Player player){
        Client client = getClient(player);
        Socket socket=client.getSocket();
        String message = arrayListSquareToString(moves) + builderToString(builder) + wantsToBuildADome(isDome);
        if(isDome){
            sendMessage("vuoi costruire la cupola?", client);
        }
        //TODO send to the player
        // wait for response
        // message= response;
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

    public void sendMessage(String message, Client client){
        Socket socket;
        if(client == null){
            for(int i=0; i<game.numberOfPlayers;i++){

            }
        }
        else{
            socket=client.getSocket();

        }
    }

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
        String stringMoves=null;
        String partial;
        for(int i=0; i<moves.size();i++){
            partial=squareToString(moves.get(i));
            stringMoves = stringMoves + " : " + partial;
        }
        stringMoves=stringMoves + "@ ";
        return stringMoves;
    }

    /**
     * converts a builder object to a string containing the coordinates of the position on the board.
     * @param builder is the builder that is needed to have converted.
     * @return a string containing the builder.
     */
    public String builderToString(Builder builder){
        Square position = builder.getPosition();
        String builderString = squareToString(position) + "@ ";
        return builderString;
    }

    /**
     * is used to set the message to signal that the player can also build a dome.
     * @param isDome if is true the player has the possibility to build a dome.
     * @return String:"1" if isDome is tru, String:"0" if the boolean is false.
     */
    public String wantsToBuildADome(Boolean isDome){
        if(isDome){
            String string = "1";
            return string;
        }
        String string = "0";
        return string;
    }

    /**
     * converts a string containing coordinates x and y separated by "," to a Square with x and y coordinates.
     * @param string is the string that contains the coordinates
     * @return a Square with x and y coordinates and all the other values set to 0.
     */
    public Square stringToSquare(String string){
        String[] coordinates = string.split(",");
        Integer x;
        Integer y;
        x = parseInt(coordinates[0]);
        y = parseInt(coordinates[1]);
        Square square = new Square(x, y);
        return square;
    }

    /**
     * converts back a string with the coordinates of the position of a builder to a builder object usable by the controller.
     * @param string the string with the coordinates x and y separated by ",".
     * @return a builder object.
     */
    public Builder stringToBuilder(String string){
        Square square = stringToSquare(string);
        Builder builder = new Builder(square, currentPlayer.playerID, null);
        return builder;
    }

    public Boolean stringToBool(String string){
        Integer x;
        x=parseInt(string);
        if(x==1){
            return true;
        }
        return false;
    }

    public Client getClient(Player player){
        for(int i=0; i<clients.size(); i++){
            if(player.clientID==clients.get(i).clientID){
                return clients.get(i);
            }
        }
        return null;
    }


}
