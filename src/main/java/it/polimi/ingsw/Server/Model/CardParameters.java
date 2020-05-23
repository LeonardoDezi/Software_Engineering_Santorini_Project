package it.polimi.ingsw.Server.Model;

public class CardParameters {
    protected String specialPhase1Moves;
    protected String specialPhase1Action;
    protected String movementPhaseMoves;
    protected String movementPhaseAction;
    protected String specialPhase2Moves;
    protected String specialPhase2Action;
    public String buildingPhaseMoves;
    protected String buildingPhaseAction;
    public boolean buildDome;
    protected String specialPhase3Moves;
    protected String specialPhase3Action;
    protected String winMovement;
    protected String winBuilding;

    public String getSpecialPhase1Moves(){
        return specialPhase1Moves;
    }

    public void setSpecialPhase1Moves(String specialPhase1Moves){
        this.specialPhase1Moves = specialPhase1Moves;
    }

    public String getSpecialPhase1Action(){
        return specialPhase1Action;
    }

    public void setSpecialPhase1Action(String specialPhase1Moves){
        this.specialPhase1Action = specialPhase1Action;
    }

    public String getMovementPhaseMoves(){
        return movementPhaseMoves;
    }

    public void setMovementPhaseMoves(String movementPhaseMoves){
        this.movementPhaseMoves = movementPhaseMoves;
    }

    public String getMovementPhaseAction(){
        return movementPhaseAction;
    }

    public void setMovementPhaseAction(String movementPhaseAction){
        this.movementPhaseAction = movementPhaseAction;
    }

    public String getSpecialPhase2Moves(){
        return specialPhase2Moves;
    }

    public void setSpecialPhase2Moves(String specialPhase2Moves){
        this.specialPhase2Moves = specialPhase2Moves;
    }

    public String getSpecialPhase2Action(){
        return specialPhase2Action;
    }

    public void setSpecialPhase2Action(String specialPhase2Action){
        this.specialPhase2Action = specialPhase2Action;
    }

    public String getBuildingPhaseMoves(){
        return buildingPhaseMoves;
    }

    public void setBuildingPhaseMoves(String buildingPhaseMoves){
        this.buildingPhaseMoves = buildingPhaseMoves;
    }

    public String getBuildingPhaseAction(){
        return buildingPhaseAction;
    }

    public void setBuildingPhaseAction(String buildingPhaseAction){
        this.buildingPhaseAction = buildingPhaseAction;
    }

    public boolean getBuildDome(){
        return buildDome;
    }

    public void setBuildDome(boolean buildDome){
        this.buildDome = buildDome;
    }

    public String getSpecialPhase3Moves(){
        return specialPhase3Moves;
    }

    public void setSpecialPhase3Moves(String specialPhase3Moves){
        this.specialPhase3Moves = specialPhase3Moves;
    }

    public String getSpecialPhase3Action(){
        return specialPhase3Action = specialPhase3Action;
    }

    public void setSpecialPhase3ActionA(String specialPhase3Action){
        this.specialPhase3Moves = specialPhase3Moves;
    }

    public String getWinMovement(){
        return winMovement;
    }

    public void setWinMovement(String winMovement){
        this.winMovement = winMovement;
    }

    public String getWinBuilding(){
        return winBuilding;
    }

    public void setWinBuilding(String winBuilding){
        this.winBuilding = winBuilding;
    }
}
