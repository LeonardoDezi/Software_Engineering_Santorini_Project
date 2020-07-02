package it.polimi.ingsw.Client.View.GUI;

import it.polimi.ingsw.Server.Model.Square;

import javax.swing.*;

public class SquareButton extends JButton {

    Square square;

    JLabel topLabel;

    private int x;
    private int y;

    public SquareButton(int x, int y){
        this.x = x;
        this.y = y;
    }


    public int getXvalue(){
        return this.x;
    }


    public int getYvalue(){
        return this.y;
    }

    public void setSquare(Square square){ this.square = square;}

    public Square getSquare(){return this.square;}

    public JLabel getTopLabel(){ return this.topLabel;}

    public void setTopLabel(JLabel label){ this.topLabel = label;}
}
