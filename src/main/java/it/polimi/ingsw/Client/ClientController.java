package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.NetworkHandler.NetInterface;
import it.polimi.ingsw.Server.Model.*;

import java.net.Socket;
import java.util.ArrayList;

public class ClientController {
    private NetInterface netInterface = new NetInterface(this);
    private Boolean stillPlaying;
    private ArrayList<Square> moves1;
    private ArrayList<Square> moves2;
    private Builder builder1;
    private  Builder builder2;

    public void matchSetup(Socket socket){
        //TODO choose cards
    }

    /**
     * starts the game phase for the client and keeps spinning until the end of the game.
     * @param socket is the socket used to talk with the server.
     */
    public void play(Socket socket){
        stillPlaying=true;
        while(stillPlaying){
            Moves moves = netInterface.getMoves(socket);
            Envelope envelope = chooseMove(moves);
            netInterface.sendMoves(envelope);
        }
        //TODO close the match
        //print message "you have lost"
        //close the connection and ask for rematch.
    }

    /**
     * Shows on the screen all the possible moves that the player can do and waits for the choice.
     * @param moves all the moves that the player can do.
     * @return the single move chosen by the player with the builder that is going to do that move.
     */
    public Envelope chooseMove(Moves moves){
        //TODO moves contains all the moves that the player can do, return an envelope to send back to the server
    }

    /**
     * sets the stillPlaying flag to false to end the game.
     */
    public void lost(){
        this.stillPlaying=false;
    }

}
