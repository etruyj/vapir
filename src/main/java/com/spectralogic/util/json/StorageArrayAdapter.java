//===================================================================
// StorageArrayAdapter.java
//      Description:
//          A Gson helper class for deserializing Storage responses that
//          can be either a single Storage object or an array of Storage objects.
//          This handles the Vail API inconsistency where /sl/api/storage
//          returns a single object when there's one storage location,
//          and an array when there are multiple.
//
// Created by Claude Code
//===================================================================

package com.spectralogic.vail.vapir.util.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.spectralogic.vail.vapir.model.Storage;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StorageArrayAdapter implements JsonDeserializer<List<Storage>> {
    
    @Override
    public List<Storage> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        
        if (json.isJsonArray()) {
            // Standard array case - multiple storage locations
            List<Storage> storageList = new ArrayList<>();
            for (JsonElement element : json.getAsJsonArray()) {
                storageList.add(context.deserialize(element, Storage.class));
            }
            return storageList;
        } else if (json.isJsonObject()) {
            // Single object case - one storage location, wrap in list
            Storage singleStorage = context.deserialize(json, Storage.class);
            List<Storage> storageList = new ArrayList<>();
            storageList.add(singleStorage);
            return storageList;
        } else {
            throw new JsonParseException("Expected JSON object or array for Storage response, but got: " + json.getClass().getSimpleName());
        }
    }
}