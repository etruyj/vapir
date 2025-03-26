//===================================================================
// SerializeObject.java
// 	Description:
// 		Converts ArrayList<Endpoint> to an ArrayList<Output>
// 		for display.
//===================================================================

package com.spectralogic.vail.vapir.ui.display.serializers;

import com.spectralogic.vail.vapir.model.Object;
import com.spectralogic.vail.vapir.model.OutputFormat;
import com.socialvagrancy.utils.storage.UnitConverter;
import java.util.ArrayList;

public class SerializeObject
{
	public static ArrayList<OutputFormat> forOutput(ArrayList<Object> list)
	{
		ArrayList<OutputFormat> output = new ArrayList<OutputFormat>();
		OutputFormat line; 

        for(Object object : list) {
            line = new OutputFormat();
            line.setHeader("object");
            line.setValue(null);
            output.add(line);

            line = new OutputFormat();
            line.setHeader("object>key");
            line.setValue(object.getKey());
            output.add(line);

            line = new OutputFormat();
            line.setHeader("object>versionId");
            line.setValue(object.getVersionId());
            output.add(line);

            line = new OutputFormat();
            line.setHeader("object>lastModified");
            line.setValue(object.getLastModified());
            output.add(line);

            line = new OutputFormat();
            line.setHeader("object>isLatest");
            if(object.isLatest() == null || object.isLatest()) {
                line.setValue("true");
            } else {
                line.setValue("false");
            }
            output.add(line);

            line = new OutputFormat();
            line.setHeader("object>etag");
            line.setValue(object.getEtag());
            output.add(line);

            line = new OutputFormat();
            line.setHeader("object>size");
            line.setValue(UnitConverter.bytesToHumanReadable(String.valueOf(object.getSize())));
            output.add(line);

            line = new OutputFormat();
            line.setHeader("object>ownerId");
            line.setValue(object.getOwnerId());
            output.add(line);

            line = new OutputFormat();
            line.setHeader("object>ownerName");
            line.setValue(object.getOwnerName());
            output.add(line);

            line = new OutputFormat();
            line.setHeader("object>storageClass");
            line.setValue(object.getStorageClass());
            output.add(line);

            // Row End Marker
            line = new OutputFormat();
            line.setHeader("object");
            line.setValue(null);
            output.add(line);
        }

        // Final Row End Marker
        line = new OutputFormat();
        line.setHeader("object");
        line.setValue(null);
        output.add(line);

		return output;
	}
}
