//===================================================================
// SerializeBucketSummary.java
// 	Description:
// 		Turns the BucketSummary variable into an output var for 
// 		display.
//===================================================================

package com.socialvagrancy.vail.ui.display.serializers;

import com.socialvagrancy.vail.structures.BucketSummary;
import com.socialvagrancy.vail.structures.OutputFormat;

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
			line.header = bucket_list.get(i).type;
			line.value = null;
			output.add(line);

			line = new OutputFormat();
			line.header = bucket_list.get(i).type + ">name";
			line.value = bucket_list.get(i).name;
			output.add(line);

			line = new OutputFormat();
			line.header = bucket_list.get(i).type + ">lifecycle";
			line.value = bucket_list.get(i).lifecycle;
			output.add(line);

			line = new OutputFormat();
			line.header = bucket_list.get(i).type + ">account";
			line.value = bucket_list.get(i).account_name;
			output.add(line);

			line = new OutputFormat();
			line.header = bucket_list.get(i).type + ">account_id";
			line.value = bucket_list.get(i).account_id;
			output.add(line);

			line = new OutputFormat();
			line.header = bucket_list.get(i).type;
			line.value = null;
			output.add(line);

		}

		return output;
	}
}
