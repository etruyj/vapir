//===================================================================
// Sava.java
// 	Description:
// 		Saves output to the specified file.
//===================================================================

package com.socialvagrancy.vail.ui.display;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Save
{
	public static void stringToFile(String line, String file_name)
	{

		try
		{
			PrintWriter writer = new PrintWriter(file_name);
			writer.print(line);
			
			writer.flush();
			writer.close();
		}
		catch(FileNotFoundException e)
		{
			System.err.println(e.getMessage());
		}

	}
}
