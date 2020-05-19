package it.polimi.ingsw.Parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.polimi.ingsw.Server.Model.Square;

import java.lang.reflect.Type;

public class MessageSerializer implements JsonSerializer <Square> {

    public JsonElement serialize(Square square, Type type,JsonSerializationContext context){
       return new JsonPrimitive(square.toString());
    }
}
