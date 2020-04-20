package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class SpecialMoves extends CardPower {
    private Card card;
    private Board board;
    private int maxHeight=1;
    private int maxBuild=1;
    private int numMoves=1;

    public SpecialMoves(Rules rules, Board board){
        this.rules=rules;
        this.board=board;
    }

    @Override
    public void movement() {
        rules.movement();
    }

    @Override
    public void building() {
        rules.building();
    }

    @Override
    public ArrayList<Square> getMovementRange() {
        return rules.getMovementRange();
    }

    @Override
    public ArrayList<Square> getBuildingRange() {
        return rules.getBuildingRange();
    }

    @Override
    public void winCondition() {
        rules.winCondition();
    }

    @Override
    public void loseCondition() {
        rules.loseCondition();
    }

    @Override
    public ArrayList<Square> getNext(Builder builder, Player player){
        this.card=player.getCard();

        if(card.effects.movement.equals("swap")){
            ArrayList<Square> specialMoves = new ArrayList<>();
            Square position = builder.getPosition();
            int i, j;
            for (i = -1; i <= 1; i++) {
                for (j = -1; j <= 1; j++) {
                    if (i == 0 && j == 0) {
                    }
                    else {
                        int playerHeight = position.getLevel();
                        int otherHeight = board.fullMap[position.x + i][position.y + j].getLevel();
                        if (playerHeight - otherHeight <= maxHeight) {
                            int newSquareValue = board.fullMap[position.x + i][position.y + j].getValue();
                            if (newSquareValue == 1) {
                                Square square = board.fullMap[position.x + i][position.y + j];
                                specialMoves.add(square);
                            }
                        }
                    }
                }
            }

            return specialMoves;
        }
        else if(card.effects.movement.equals("1")){
            
        }

    }
}
