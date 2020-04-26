package it.polimi.ingsw.Model;

public class WinMoveDown extends CardPower {

    public WinMoveDown(Game game, Card card){

        super(game, card);

    }

    @Override
    public void movement(Builder builder, int x, int y) {
        Square position = builder.getPosition();
        if (position.getLevel() - game.gameBoard.fullMap[x][y].getLevel() >= 2) {

            switch(card.power){

                case "Pan":
                    //vinci();
                    game.gameBoard.move(builder, x, y);
                break;

                case "Chronus":
                    //vinci con crono;
            }

        } else
            basic.movement(builder, x, y);

    }

}
