//===================================================================
// XML.java
// 	Formats the OutputFormat Array as an XML table.
//===================================================================

package com.spectralogic.vail.vapir.ui.display;

import com.spectralogic.vail.vapir.model.OutputFormat;

import java.util.ArrayList;

public class XML
{
	public static void display(ArrayList<OutputFormat> output)
	{
		int indent = 0;
		String last_header = "";
		String[] headers;
		String formatted_header;
		String line;

		for(int i=0; i<output.size(); i++)
		{
			headers = output.get(i).getHeader().split(">");

			if(output.get(i).getValue() == null)
			{
				// Header field
				if(headers[headers.length-1].equals(last_header))
				{
					// Closing header
					indent--;
					
					formatted_header = "</" + headers[headers.length-1] + ">";
					Print.line("", formatted_header, indent, false);

					if(headers.length-2>=0)
					{
						last_header = headers[headers.length-2];
					}
					else
					{
						last_header = "";
					}
				}
				else
				{
					// Opening header
					formatted_header = "<" + headers[headers.length-1] + ">";
					Print.line("", formatted_header, indent, false);
					last_header = headers[headers.length-1];
					indent++;
				}
			}
			else
			{
				// Print Value.
				formatted_header = "<" + headers[headers.length-1] + ">";
				line = formatted_header + output.get(i).getValue();
			
				formatted_header = "</" + headers[headers.length-1] + ">";
				line = line + formatted_header;

				Print.line("", line, indent, false);				
			}
		}
	}
}
