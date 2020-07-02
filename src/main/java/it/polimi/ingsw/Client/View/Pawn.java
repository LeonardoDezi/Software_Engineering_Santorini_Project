package it.polimi.ingsw.Client.View;

import it.polimi.ingsw.Client.View.CLI.Colour;

import java.util.HashMap;

/**
 * this class represents the pawns of the game
 */
public class Pawn {

   // private Cell position;

    /** represents the colour of pawn by player */
    private String colour;

    /** represents the sex of the pawn */
    private String sex;

    private HashMap<String, String> gender;
    private HashMap<String, String> colours;

    /**
     * create and initialize the pawn
     * @param colour is player's colour
     * @param sex is the pawn sex
     */
    public Pawn(String colour, String sex){
    //    this.position = position;
        this.colour = colour;
        this.sex = sex;
        maps();
    }

    /** initialize the hashMaps of the pawn representation */
    public void maps(){
        gender = new HashMap<>();
        colours = new HashMap<>();

        gender.put("Male", "\u2642");
        gender.put("Female", "\u2640");

        colours.put("red", Colour.ANSI_RED);
        colours.put("blue", Colour.ANSI_BLUE);
        colours.put("white", Colour.RESET);
    }

    /**
     * gives back the map of the sex types
     * @return gender map
     */
    public HashMap<String, String> getGender(){
        return gender;
    }

    /**
     * gives back the sex of the pawn
     * @return sex
     */
    public String getSex() {
        return sex;
    }

    /**
     * gives back the map of the colours
     * @return colours map
     */
    public HashMap<String, String> getColours() {
        return colours;
    }

    /**
     * gives back the colour of the pawn
     * @return colour
     */
    public String getColour() {
        return colour;
    }

    public void setSex(String builderSex){
        this.sex = builderSex;
    }

    /**
     * sets the pawn colour for the player
     * @param colour is the player's colour
     */
    public void setColour(String colour){
        this.colour = colour;
    }
}
                                                          