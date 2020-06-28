package it.polimi.ingsw.Client.View.GUI;

import javax.swing.*;

public class SquareButton extends JButton {

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
}
