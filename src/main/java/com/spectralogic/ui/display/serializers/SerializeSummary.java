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
import java.util.List;

public class SerializeSummary
{
	public static ArrayList<OutputFormat> forOutput(List<Summary> list)
	{
		ArrayList<OutputFormat> output = new ArrayList<OutputFormat>();
		OutputFormat line; 

		for(Summary summary : list)
		{
			line = new OutputFormat();
			line.setHeader(summary.getType());
			line.setValue(null);
			output.add(line);

			line = new OutputFormat();
			line.setHeader(summary.getType() + ">name");
			line.setValue(summary.getName());
			output.add(line);

			line = new OutputFormat();
			line.setHeader(summary.getType() + ">accountName");
			line.setValue(summary.getAccountName());
			output.add(line);

			line = new OutputFormat();
			line.setHeader(summary.getType() + ">accountId");
			line.setValue(summary.getAccountId());
			output.add(line);

			if(summary.getStatus() != null)
			{
				line = new OutputFormat();
				line.setHeader(summary.getType() + ">status");
				line.setValue(summary.getStatus());
				output.add(line);
			}

			line = new OutputFormat();
			line.setHeader(summary.getType());
			line.setValue(null);
			output.add(line);
		
		}

		return output;
	}
}
