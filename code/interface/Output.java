//===================================================================
// Output.java
// 	Prints the output for the different scripts.
//===================================================================

package com.socialvagrancy.vail.ui;

import com.socialvagrancy.vail.structures.OutputFormat;
import com.socialvagrancy.vail.ui.display.Print;
import com.socialvagrancy.vail.ui.display.Table;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;

public class Output
{

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
		}
	}

	//==============================================
	//	PRINT FUNCTIONS
	//==============================================

	public static void printDeliminator(int columns, int column_width)
	{
		//Print top
		for(int i = 0; i<(column_width*columns); i++)
		{
			if(i%column_width==0)
			{
				System.out.print("+");
			}
			else
			{
				System.out.print("-");
			}
		}
		System.out.println("+");
	}

	public static void formatTable(ArrayList<OutputFormat> output)
	{
		int line = 0;
		int columns = 0;
		int i = 0;
		int indent = 0;
		boolean building_table = true;
		boolean build_successful = true;

		String current_heading = "none";
		String[] headers;

		ArrayList<String> headings = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();

		// Build the table;
		while(building_table)
		{	
			headers = output.get(i).header.split(">");
		
		
			// Print categories.	
			if(output.get(i).value == null)
			{
		
				if(!headers[headers.length-1].equals(current_heading))
				{
					// Current header isn't the same as the reference.
					indent++;
					current_heading = headers[headers.length-1];

				}
				else
				{
					indent--;
					line++;
					// Make sure we're deeper than the root 
					// element of the tree.
					if(headers.length>1)
					{
						current_heading = headers[headers.length-2];
					}
					else
					{
						current_heading = "";
					}
				}
			}
			else
			{
				if(indent != 1)
				{
					// Multi-tiered data
					// Unable to use this output format
					building_table = false;
					build_successful = false;
				}

				headings.add(headers[1]);
				values.add(output.get(i).value);
				
				if(line == 0)
				{
					columns++;
				}
			}

			i++;

			if(i >= output.size())
			{
				building_table=false;
			}
		}

		if(build_successful)
		{
			printTable(headings, values, columns);
		}
		else
		{
			// Print in shell format if table doesn't fit.
			Print.shell(output);
		}

	}

	public static void printTable(ArrayList<String> headers, ArrayList<String> values, int columns)
	{
		int column = 0;
		int column_width = 30;
		int margin = 2;

		// Print Top Bar
		printDeliminator(columns, column_width);

		// Print headers
		for(int i = 0; i < columns; i++)
		{
			int padding = column_width - margin - headers.get(i).length() - 1;
			System.out.print("|");
		
			for(int j=0; j<margin; j++)
			{
				System.out.print(" ");
			}

			System.out.print(headers.get(i));

			for(int k=0; k<padding; k++)
			{
				System.out.print(" ");
			}
		}
		System.out.println("|");
		
		// Print Bar
		printDeliminator(columns, column_width);
		
		// Print Values
		for(int i = 0; i < values.size(); i++)
		{
			int padding = column_width - margin - values.get(i).length() - 1;
			System.out.print("|");
		
			for(int j=0; j<margin; j++)
			{
				System.out.print(" ");
			}

			System.out.print(values.get(i));

			for(int k=0; k<padding; k++)
			{
				System.out.print(" ");
			}

			column++;

			if(column == columns)
			{
				System.out.println("|");

				column = 0;
				// Print Deliminator
				printDeliminator(columns, column_width);
			}
		
		}
	}
}
