package it.polimi.ingsw.Observer;

import it.polimi.ingsw.Server.Client;

import java.io.IOException;

public interface Observer<T> {

        Integer update(Client client) throws IOException;
}

