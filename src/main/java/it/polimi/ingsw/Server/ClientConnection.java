package it.polimi.ingsw.Server;

import it.polimi.ingsw.Observer.Observer;

public interface ClientConnection {
        void closeConnection(Client client);
        void asyncSend(Object message);
}
