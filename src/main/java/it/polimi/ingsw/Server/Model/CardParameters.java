package it.polimi.ingsw.Server.Model;

/**
 * represents the parameters of a card.
 * The parameters will be used during a game to decide the correct behaviour and the actions permitted to the
 * card's owner
 */
public class CardParameters {
    /** represents the special effect of the card when defining the moves during specialPhase1. if null, the card has no special effect */
    protected String specialPhase1Moves;

    /** represents the special effect of the card when defining the actions during specialPhase1. if null, the card has no special effect */
    protected String specialPhase1Action;

    /** represents the special effect of the card when defining the moves during movementPhase. if null, the card has no special effect */
    protected String movementPhaseMoves;

    /** represents the special effect of the card when defining the actions during movementPhase. if null, the card has no special effect */
    protected String movementPhaseAction;

    /** represents the special effect of the card when defining the moves during specialPhase2. if null, the card has no special effect */
    protected String specialPhase2Moves;

    /** represents the special effect of the card when defining the actions during specialPhase2. if null, the card has no special effect */
    protected String specialPhase2Action;

    /** represents the special effect of the card when defining the moves during buildingPhase. if null, the card has no special effect */
    public String buildingPhaseMoves;

    /** represents the special effect of the card when defining the actions during buildingPhase. if null, the card has no special effect */
    protected String buildingPhaseAction;

    /** represents the possibility of the owner's card to build a dome during its turn, even if the result is not a completed tower */
    public boolean buildDome;

    /** represents the special effect of the card when defining the moves during specialPhase3. if null, the card has no special effect */
    protected String specialPhase3Moves;

    /** represents the special effect of the card when defining the actions during specialPhase3. if null, the card has no special effect */
    protected String specialPhase3Action;

    /** represents the special effect of the card regarding winning movements. if null, the card has no special effect */
    protected String winMovement;

    /** represents the special effect of the card regarding winning building actions. if null, the card has no special effect */
    protected String winBuilding;



/*   TODO  CANCELLARE TUTTO E CONTROLLARE CHE NON DIA PROBLEMI
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

 */
}
