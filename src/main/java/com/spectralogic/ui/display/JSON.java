//===================================================================
// JSON.java
// 	Description:
// 		Handles formatting and output for JSON files. Can either
// 		be written to the shell or output to a file.
//		This is kept separate from the serializer code as 
//		the serializer's primary function is to convert classes
//		into OutputFormat for display.
//===================================================================

package com.spectralogic.vail.vapir.ui.display;

import com.spectralogic.vail.vapir.model.SphereConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

public class JSON
{
	public static String formatJson(SphereConfig config)
	{
		String json = "{FAILED}";

		try
		{
			Gson gson = new GsonBuilder().setPrettyPrinting().create();

			json = gson.toJson(config);
		}
		catch(JsonParseException e)
		{
			System.err.println(e.getMessage());
		}

		return json;
	}
}
