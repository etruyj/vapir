//===================================================================
// SerializeSummary.java
// 	Description:
// 		Converts ArrayList<Summary> to an ArrayList<Output>
// 		for display.
//===================================================================

package com.socialvagrancy.vail.ui.display.serializers;

import com.socialvagrancy.vail.structures.OutputFormat;
import com.socialvagrancy.vail.structures.Summary;
import java.util.ArrayList;

public class SerializeSummary
{
	public static ArrayList<OutputFormat> forOutput(ArrayList<Summary> list)
	{
		ArrayList<OutputFormat> output = new ArrayList<OutputFormat>();
		OutputFormat line; 

		for(int i=0; i<list.size(); i++)
		{
			line = new OutputFormat();
			line.header = list.get(i).type;
			line.value = null;
			output.add(line);

			line = new OutputFormat();
			line.header = list.get(i).type + ">name";
			line.value = list.get(i).name;
			output.add(line);

			line = new OutputFormat();
			line.header = list.get(i).type + ">accountName";
			line.value = list.get(i).account_name;
			output.add(line);

			line = new OutputFormat();
			line.header = list.get(i).type + ">accountId";
			line.value = list.get(i).account_id;
			output.add(line);

			if(list.get(i).status != null)
			{
				line = new OutputFormat();
				line.header = list.get(i).type + ">status";
				line.value = list.get(i).status;
				output.add(line);
			}

			line = new OutputFormat();
			line.header = list.get(i).type;
			line.value = null;
			output.add(line);
		
		}

		return output;
	}
}
