//===================================================================
// SerializeBucketDetails.java
// 	Description:
// 		Converts ArrayList<BucketDetails> to an ArrayList<Output>
// 		for display.
//===================================================================

package com.spectralogic.vail.vapir.ui.display.serializers;

import com.spectralogic.vail.vapir.model.report.BucketDetails;
import com.spectralogic.vail.vapir.model.report.CloneLocation;
import com.spectralogic.vail.vapir.model.OutputFormat;
import com.socialvagrancy.utils.storage.UnitConverter;
import java.util.ArrayList;

public class SerializeBucketDetails {
	public static ArrayList<OutputFormat> forOutput(BucketDetails bucket)
	{
		ArrayList<OutputFormat> output = new ArrayList<OutputFormat>();
		OutputFormat line; 

        line = new OutputFormat();
        line.setHeader("bucket");
        line.setValue(null);
        output.add(line);

        line = new OutputFormat();
        line.setHeader("bucket>name");
        line.setValue(bucket.getName());
        output.add(line);

        line = new OutputFormat();
        line.setHeader("bucket>objectCount");
        line.setValue(String.valueOf(bucket.getObjects()));
        output.add(line);

        line = new OutputFormat();
        line.setHeader("bucket>totalSize");
        line.setValue(UnitConverter.bytesToHumanReadable(String.valueOf(bucket.getSize())));
        output.add(line);

        // Clone Info
        for(String key : bucket.getClones().keySet()) {
            line = new OutputFormat();
            line.setHeader("bucket>[" + key + "]_clones");
            line.setValue(String.valueOf(bucket.getClones().get(key).getClones()));
            output.add(line);
            
            line = new OutputFormat();
            line.setHeader("bucket>[" + key + "]_size");
            line.setValue(UnitConverter.bytesToHumanReadable(String.valueOf(bucket.getClones().get(key).getSize())));
            output.add(line);
        }


        // Final Row End Marker
        line = new OutputFormat();
        line.setHeader("bucket");
        line.setValue(null);
        output.add(line);

		return output;
	}
}
