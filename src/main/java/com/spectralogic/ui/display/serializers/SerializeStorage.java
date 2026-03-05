
//===================================================================
// SerializeStorage.java
// 	Description: Serializes Storage objects for output formatting
//===================================================================

package com.spectralogic.vail.vapir.ui.display.serializers;

import com.spectralogic.vail.vapir.model.OutputFormat;
import com.spectralogic.vail.vapir.model.Storage;

import java.util.ArrayList;
import java.util.List;

public class SerializeStorage {

    public static ArrayList<OutputFormat> forOutput(List<Storage> storage) {
        ArrayList<OutputFormat> output = new ArrayList<OutputFormat>();
        OutputFormat line;

        for(Storage storageItem : storage) {
            line = new OutputFormat();
            line.setHeader("storage");
            line.setValue(null);
            output.add(line);

            line = new OutputFormat();
            line.setHeader("storage>name");
            line.setValue(storageItem.getName());
            output.add(line);

            line = new OutputFormat();
            line.setHeader("storage>type");
            line.setValue(storageItem.getType());
            output.add(line);

            line = new OutputFormat();
            line.setHeader("storage>class");
            line.setValue(storageItem.getStorageClass());
            output.add(line);

            line = new OutputFormat();
            line.setHeader("storage>status");
            line.setValue(storageItem.getStatus());
            output.add(line);
            
            line = new OutputFormat();
            line.setHeader("storage");
            line.setValue(null);
            output.add(line);
        }

        return output;
    }
}
