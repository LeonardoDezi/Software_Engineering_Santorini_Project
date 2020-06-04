package it.polimi.ingsw.Server.Model;

/**
 * represents the parameters of a card.
 * The parameters will be used during a game to decide the correct behaviour and the actions permitted to the
 * card's owner
 */
public class CardParameters {
    /**
     * represents the special effect of the card when defining the moves during specialPhase1. if null, the card has no special effect
     */
    protected String specialPhase1Moves;

    /**
     * represents the special effect of the card when defining the actions during specialPhase1. if null, the card has no special effect
     */
    protected String specialPhase1Action;

    /**
     * represents the special effect of the card when defining the moves during movementPhase. if null, the card has no special effect
     */
    protected String movementPhaseMoves;

    /**
     * represents the special effect of the card when defining the actions during movementPhase. if null, the card has no special effect
     */
    protected String movementPhaseAction;

    /**
     * represents the special effect of the card when defining the moves during specialPhase2. if null, the card has no special effect
     */
    protected String specialPhase2Moves;

    /**
     * represents the special effect of the card when defining the actions during specialPhase2. if null, the card has no special effect
     */
    protected String specialPhase2Action;

    /**
     * represents the special effect of the card when defining the moves during buildingPhase. if null, the card has no special effect
     */
    protected String buildingPhaseMoves;

    /**
     * represents the special effect of the card when defining the actions during buildingPhase. if null, the card has no special effect
     */
    protected String buildingPhaseAction;

    /**
     * represents the possibility of the owner's card to build a dome during its turn, even if the result is not a completed tower
     */
    protected boolean buildDome;

    /**
     * represents the special effect of the card when defining the moves during specialPhase3. if null, the card has no special effect
     */
    protected String specialPhase3Moves;

    /**
     * represents the special effect of the card when defining the actions during specialPhase3. if null, the card has no special effect
     */
    protected String specialPhase3Action;

    /**
     * represents the special effect of the card regarding winning movements. if null, the card has no special effect
     */
    protected String winMovement;

    /**
     * represents the special effect of the card regarding winning building actions. if null, the card has no special effect
     */
    protected String winBuilding;

}

