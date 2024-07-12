//===================================================================
// SerializeSummary.java
// 	Description:
// 		Converts ArrayList<Summary> to an ArrayList<Output>
// 		for display.
//===================================================================

package com.spectralogic.vail.vapir.ui.display.serializers;

import com.spectralogic.vail.vapir.model.OutputFormat;
import com.spectralogic.vail.vapir.model.Summary;
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
			line.setHeader(list.get(i).getType());
			line.setValue(null);
			output.add(line);

			line = new OutputFormat();
			line.setHeader(list.get(i).getType() + ">name");
			line.setValue(list.get(i).getName());
			output.add(line);

			line = new OutputFormat();
			line.setHeader(list.get(i).getType() + ">accountName");
			line.setValue(list.get(i).getAccountName());
			output.add(line);

			line = new OutputFormat();
			line.setHeader(list.get(i).getType() + ">accountId");
			line.setValue(list.get(i).getAccountId());
			output.add(line);

			if(list.get(i).getStatus() != null)
			{
				line = new OutputFormat();
				line.setHeader(list.get(i).getType() + ">status");
				line.setValue(list.get(i).getStatus());
				output.add(line);
			}

			line = new OutputFormat();
			line.setHeader(list.get(i).getType());
			line.setValue(null);
			output.add(line);
		
		}

		return output;
	}
}
