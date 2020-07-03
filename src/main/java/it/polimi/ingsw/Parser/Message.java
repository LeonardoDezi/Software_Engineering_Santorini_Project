package it.polimi.ingsw.Parser;

/**
 * represents the messages that arrive from the server and get send to it from client
 */
public class Message {

    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
