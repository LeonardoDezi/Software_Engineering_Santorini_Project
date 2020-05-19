package it.polimi.ingsw.Parser;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class MessageSerializer {
    public void clientServerMessage() {

        Gson gson = new GsonBuilder().create();

        Box<Message> type = new Box<>();
        Message message = new Message();
        message.setId();
        message.setMoves();
        message.setBuilder();

        Type fooType = new TypeToken<Box<Message>>() {}.getType();
        String userJson = gson.toJson(type, fooType);

        Box <Message> box = gson.fromJson(userJson, fooType);

    }
}

class Box <T> {
    private T t;

    public void set(T t) {
        this.t = t;
    }

    public T get(){
        return t;
    }
}