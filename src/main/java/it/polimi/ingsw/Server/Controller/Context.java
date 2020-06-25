package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Server.Model.Phase;
import it.polimi.ingsw.Server.VirtualView.NetInterface;

import java.io.IOException;

/** this class represents the class Context in a State Pattern */
public class Context {
    /** the phase which the turn is currently executing */
    private Phase currentPhase;
    /** the interface used by the server to communicate with the clients */
    private final NetInterface netInterface;

    /**
     * creates a new Context
     * @param netInterface is the interface used by the server to communicate with the clients
     * */
    public Context(NetInterface netInterface){
        this.netInterface = netInterface;
    }

    /**
     * sets the currentPhase
      * @param phase is the phase that is to be set as currentPhase
     */
    public void setPhase(Phase phase) { currentPhase = phase; }

    /**
     * gets the currentPhase
     * @return the currentPhase saved in the context
     */
    public Phase getPhase(){ return currentPhase; }

    /**
     * calls the handle() method of the current phase
     */
    public void request() throws IOException { currentPhase.handle(); }

    /**
     * gets the netInterface
     * @return the netInterface saved in the context
     */
    public NetInterface getNetInterface(){ return this.netInterface;}

}
