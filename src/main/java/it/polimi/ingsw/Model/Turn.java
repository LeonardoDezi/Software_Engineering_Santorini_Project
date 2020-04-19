package it.polimi.ingsw.Model;

public class Turn {
    private Player player;
    public Context context;
    private Game game;

    public Turn(Player player, Game game) {
        this.player = player;
        //this.context ?
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void nextPlayer() {
        int size = game.playerList.size();
        if (this.player.equals(game.playerList.get(size))) {
            this.player = game.playerList.get(0);
        } else {
            int i = game.playerList.indexOf(player);
            player = game.playerList.get(i + 1);
        }

    }
}