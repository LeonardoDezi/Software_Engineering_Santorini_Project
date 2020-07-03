package it.polimi.ingsw.Client.View.GUI;

import it.polimi.ingsw.Server.Model.Square;

import javax.swing.*;

/** this class represents the button of the board */
public class SquareButton extends JButton {

    /** the square associated to the squarebutton */
    Square square;

    /** the label on top of the squareButton. It can be either a dome or a worker */
    JLabel topLabel;

    /** the x coordinate */
    private int x;

    /** the y coordinate */
    private int y;

    public SquareButton(int x, int y){
        this.x = x;
        this.y = y;
    }

    /** returns the x coordinate */
    public int getXvalue(){
        return this.x;
    }

    /** returns the y coordinate */
    public int getYvalue(){
        return this.y;
    }

    public void setSquare(Square square){ this.square = square;}

    public Square getSquare(){return this.square;}

    public JLabel getTopLabel(){ return this.topLabel;}

    public void setTopLabel(JLabel label){ this.topLabel = label;}
}
