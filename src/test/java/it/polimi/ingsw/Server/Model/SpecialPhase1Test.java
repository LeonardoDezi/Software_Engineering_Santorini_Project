package it.polimi.ingsw.Server.Model;

public class SpecialPhase1Test {
    private SpecialPhase1 specialPhase1;
    private Player player;

    public SpecialPhase1Test(){
        Game game = new Game();
        specialPhase1 = new SpecialPhase1(game);
        player = new Player("Marco", "Red", game);
    }
}
