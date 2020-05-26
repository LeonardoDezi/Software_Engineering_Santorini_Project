package it.polimi.ingsw.Client.NetworkHandler;

import it.polimi.ingsw.Client.ClientController;
import it.polimi.ingsw.Client.Moves;
import it.polimi.ingsw.Server.Model.*;
import java.net.Socket;
import java.util.ArrayList;
import static java.lang.Integer.parseInt;

public class NetInterface {
    private Sender sender;
    private Reciever reciever;
    private ArrayList<Square> moves1;
    private ArrayList<Square> moves2;
    private Builder builder1;
    private Builder builder2;
    private ClientController clientController;

    public NetInterface(ClientController clientController) {
        this.clientController = clientController;
    }

    public Moves getMoves(Socket socket){
        String availableMoves = reciever.recieve(socket);
        Moves moves = null;
        String[] values = availableMoves.split("@");
        if(values[0].equals("-1")){
            return null;
        }
        if(values[0].equals("1")){
            moves1 = stringToArrayListSquare(values[1]);
            builder1 = stringToBuilder(values[2]);
            moves2 = stringToArrayListSquare(values[3]);
            builder2 = stringToBuilder(values[4]);
            moves = new Moves(builder1, moves1, builder2, moves2, false, false);
        }
        if(values[0].equals("2")){
            moves1 = stringToArrayListSquare(values[1]);
            builder2 = stringToBuilder(values[2]);
            moves = new Moves(builder1, moves1, null, null, false, false);
        }
        if(values[0].equals("3")){
            moves1 = stringToArrayListSquare(values[1]);
            builder2 = stringToBuilder(values[2]);
            boolean isDome = stringToBool(values[3]);
            moves = new Moves(builder1, moves1, null, null, isDome, false);
        }
        if(values[0].equals("4")){
            moves1 = stringToArrayListSquare(values[1]);
            builder1 = stringToBuilder(values[2]);
            moves2 = stringToArrayListSquare(values[3]);
            builder2 = stringToBuilder(values[4]);
            boolean female = stringToBool(values[5]);
            moves = new Moves(builder1, moves1, builder2, moves2, false, female);
        }
        if(values[0].equals("5")){ //text message

            moves = null;
        }
        if(values[0].equals("6")){ //the client loses
            clientController.lost();
        }
        return moves;
    }

    public void sendMoves(Envelope envelope){
        if(envelope == null){
            //TODO send 0
        }
        Square square = envelope.getMove();
        Builder builder = envelope.getBuilder();
        Boolean dome = envelope.getIsDome();
        String coordinates = squareToString(square) + builderToString(builder) + booleanToString(dome);
        //TODO send to the server
        return;
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
     * converts a Square object to a String containing the coordinates
     * @param square is the square that is going to be converted
     * @return a string with the x and y coordinates of the square separated by ","
     */
    public String squareToString(Square square){
        return square.x + "," + square.y;
    }

    /**
     * converts back a string with the coordinates of the position of a builder to a builder object usable by the controller.
     * @param string the string with the coordinates x and y separated by ",".
     * @return a builder object.
     */
    public Builder stringToBuilder(String string){
        Square square = stringToSquare(string);
        return new Builder(square, null, null);
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

}
