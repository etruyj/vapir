//===================================================================
// Output.java
// 	Prints the output for the different scripts.
//===================================================================

package com.socialvagrancy.vail.ui;

import com.socialvagrancy.vail.structures.OutputFormat;
import com.socialvagrancy.vail.ui.display.Print;
import com.socialvagrancy.vail.ui.display.Table;
import com.socialvagrancy.vail.ui.display.XML;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;

public class Output
{
	public static void printHelp(String filename)
	{
		Print.help(filename);
	}

	public static void print(ArrayList<String> response)
	{
		for(int i=0; i<response.size(); i++)
		{
			Print.line("none", response.get(i), 0, false); 
		}
	}

	public static void print(String singleResponse)
	{
		// Print a single line response from a query
		// Used for failed queries and when there is no info.
		Print.line("none", singleResponse, 0, false);
	}

	public static void print(ArrayList<OutputFormat> output, String output_format)
	{
		switch(output_format)
		{
			case "debug":
				Print.debug(output);
				break;
			case "shell":
				Print.shell(output);
				break;
			case "CSV":
			case "csv":
			case "table":
				Table.format(output, output_format);
				break;
			case "XML":
			case "xml":
				XML.display(output);
				break;
		}
	}

}
