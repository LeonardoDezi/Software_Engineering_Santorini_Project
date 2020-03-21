package Model;

public class Player {
    private boolean isplayertur;
    //elemento di tipo carta
    private String playerID;
    private Builder[2] builders;
    private boolean isCardDealer;

    public Player(String playerID) {
        this.playerID = playerID;
    }


    public void setIsplayertur(boolean isplayertur) {
        this.isplayertur = isplayertur;
    }

    //metodo getcard, bisogna capire bene come gestire le carte per poter fare questa parte


    public String getPlayerID() {
        return playerID;
    }

}
