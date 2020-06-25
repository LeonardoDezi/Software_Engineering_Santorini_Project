package it.polimi.ingsw.Client.View;

import it.polimi.ingsw.Client.View.CLI.Colour;

import java.util.HashMap;

public class Pawn {

   // private Cell position;

    private String colour;

    private String sex;

    private HashMap<String, String> gender;
    private HashMap<String, String> colours;

    public Pawn(String colour, String sex){
    //    this.position = position;
        this.colour = colour;
        this.sex = sex;
        maps();
    }

    public void maps(){
        gender = new HashMap<>();
        colours = new HashMap<>();

        gender.put("Male", "\u2642");
        gender.put("Female", "\u2640");

        colours.put("red", Colour.ANSI_RED);
        colours.put("blue", Colour.ANSI_BLUE);
        colours.put("white", Colour.RESET);
    }

    public HashMap<String, String> getGender(){
        return gender;
    }

    public String getSex() {
        return sex;
    }

    public HashMap<String, String> getColours() {
        return colours;
    }

    public String getColour() {
        return colour;
    }

    public void setSex(String builderSex){
        this.sex = builderSex;
    }

    public void setColour(String colour){
        this.colour = colour;
    }
}
                                                          