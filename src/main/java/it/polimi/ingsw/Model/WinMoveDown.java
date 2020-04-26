package it.polimi.ingsw.Model;

public class WinMoveDown extends CardPower {

    public WinMoveDown(){

        super();

    }

    @Override
    public void movement(Builder builder, int x, int y) {
        Square position = builder.getPosition();
        if (position.getLevel() - fullMap[x][y].getLevel() >= 2) {
            vinci();
        } else
            rules.winCondition(builder, x, y);

        rules.movement(builder, x, y);

    }

}
