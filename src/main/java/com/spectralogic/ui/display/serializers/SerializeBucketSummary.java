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

public class SerializeBucketSummary
{
	public static ArrayList<OutputFormat> forOutput(ArrayList<BucketSummary> bucket_list)
	{
		ArrayList<OutputFormat> output = new ArrayList<OutputFormat>();
		OutputFormat line;

		for(int i=0; i < bucket_list.size(); i++)
		{
			line = new OutputFormat();
			line.setHeader(bucket_list.get(i).getType());
			line.setValue(null);
			output.add(line);

			line = new OutputFormat();
			line.setHeader(bucket_list.get(i).getType() + ">name");
			line.setValue(bucket_list.get(i).getName());
			output.add(line);

			line = new OutputFormat();
			line.setHeader(bucket_list.get(i).getType() + ">lifecycle");
			line.setValue(bucket_list.get(i).getLifecycle());
			output.add(line);

			line = new OutputFormat();
			line.setHeader(bucket_list.get(i).getType() + ">account");
			line.setValue(bucket_list.get(i).getAccountName());
			output.add(line);

			line = new OutputFormat();
			line.setHeader(bucket_list.get(i).getType() + ">account_id");
			line.setValue(bucket_list.get(i).getAccountId());
			output.add(line);

			line = new OutputFormat();
			line.setHeader(bucket_list.get(i).getType());
			line.setValue(null);
			output.add(line);

		}

		return output;
	}
}
