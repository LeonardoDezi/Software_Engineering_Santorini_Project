package it.polimi.ingsw.Observer;

import java.io.IOException;

public interface Observer<T> {

        void update() throws IOException;
}

