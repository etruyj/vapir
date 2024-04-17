//===================================================================
// ArrayOrStringAdapter.java
//      Description:
//          A Gson helper class for serializing and deserializing
//          when the value can be either an Array or a String.
//===================================================================

package com.socialvagrancy.vail.utils.http;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ArrayOrStringAdapter implements JsonSerializer<ArrayList<String>>, JsonDeserializer<ArrayList<String>> {
    @Override
    public ArrayList<String> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ArrayList<String> array_list = new ArrayList<>();

        if(json.isJsonArray()) {
            for(JsonElement element : json.getAsJsonArray()) {
                array_list.add(element.getAsString());
            }
        } else if(json.isJsonPrimitive()) {
            array_list.add(json.getAsString());
        } else {
            throw new JsonParseException("Invalid JSON format for field.");
        }
    
        return array_list;
    }

    @Override
    public JsonElement serialize(ArrayList<String> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonElement element = context.serialize(src);

        return element;
    }
}
