package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Server.Model.Phase;
import it.polimi.ingsw.Server.VirtualView.NetInterface;

import java.io.IOException;

public class Context {

    private Phase currentPhase;

    private final NetInterface netInterface;

    public Context(NetInterface netInterface)
        {
            this.netInterface = netInterface;
        }

    public void setPhase(Phase phase)
        {
            currentPhase = phase;
        }

    public Phase getPhase(){ return currentPhase; }

    public void request() throws IOException { currentPhase.handle(); }

    public NetInterface getNetInterface(){ return this.netInterface;}

}
