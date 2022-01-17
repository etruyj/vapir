//===================================================================
// Display.java
// 	Prints the output for the different scripts.
//===================================================================

package com.socialvagrancy.vail.ui.display;

import com.socialvagrancy.vail.structures.Account;
import com.socialvagrancy.vail.structures.Bucket;
import com.socialvagrancy.vail.structures.OutputFormat;
import com.socialvagrancy.vail.structures.Summary;
import com.socialvagrancy.vail.structures.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;

public class Display
{
	public static void output(Account[] accounts, String output_format)
	{
		ArrayList<OutputFormat> output = Serializer.convert(accounts);

		print(output, output_format);
	}

	public static void output(Bucket[] buckets, String output_format)
	{
		ArrayList<OutputFormat> output = Serializer.convert(buckets);

		print(output, output_format);
	}

	public static void output(String result, String output_format)
	{
		switch(output_format)
		{
			// Classes without need for special formatting.
			case "csv":
			case "table":
			case "shell":
				print(result);
				break;
			default:
				ArrayList<OutputFormat> output = Serializer.convert(result);
				print(output, output_format);
				break;
		}

	}
	
	public static void output(ArrayList<Summary> summary, String output_format)
	{
		ArrayList<OutputFormat> output = Serializer.convert(summary);

		print(output, output_format);
	}

	public static void output(User[] users, String output_format)
	{
		ArrayList<OutputFormat> output = Serializer.convert(users);

		print(output, output_format);
	}

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

	private static void print(ArrayList<OutputFormat> output, String output_format)
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
