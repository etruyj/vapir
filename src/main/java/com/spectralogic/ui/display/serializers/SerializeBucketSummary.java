//===================================================================
// SerializeBucketSummary.java
// 	Description:
// 		Turns the BucketSummary variable into an output var for 
// 		display.
//===================================================================

package com.spectralogic.vail.vapir.ui.display.serializers;

import com.spectralogic.vail.vapir.model.BucketSummary;
import com.spectralogic.vail.vapir.model.OutputFormat;

import java.util.ArrayList;
import java.util.List;

public class SerializeBucketSummary
{
	public static ArrayList<OutputFormat> forOutput(List<BucketSummary> bucket_list)
	{
		ArrayList<OutputFormat> output = new ArrayList<OutputFormat>();
		OutputFormat line;

		for(BucketSummary bucket : bucket_list)
		{
			line = new OutputFormat();
			line.setHeader(bucket.getType());
			line.setValue(null);
			output.add(line);

			line = new OutputFormat();
			line.setHeader(bucket.getType() + ">name");
			line.setValue(bucket.getName());
			output.add(line);

			line = new OutputFormat();
			line.setHeader(bucket.getType() + ">lifecycle");
			line.setValue(bucket.getLifecycle());
			output.add(line);

			line = new OutputFormat();
			line.setHeader(bucket.getType() + ">account");
			line.setValue(bucket.getAccountName());
			output.add(line);

			line = new OutputFormat();
			line.setHeader(bucket.getType() + ">account_id");
			line.setValue(bucket.getAccountId());
			output.add(line);

			line = new OutputFormat();
			line.setHeader(bucket.getType());
			line.setValue(null);
			output.add(line);

		}

		return output;
	}
}
