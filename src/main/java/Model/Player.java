package Model;

public class Player {
    private boolean isPlayerTurn;
    //elemento di tipo carta
    private String playerID;
    private Builder[] builders;
    private boolean isCardDealer;

    public Player(String playerID) {
        this.playerID = playerID;
    }


    public void setIsPlayerTurn(boolean isPlayerTurn) {
        this.isPlayerTurn = isPlayerTurn;
    }

    //metodo getcard, bisogna capire bene come gestire le carte per poter fare questa parte


    public String getPlayerID() {
        return playerID;
    }

}
