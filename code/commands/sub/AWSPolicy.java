//===================================================================
// AWSPolicy.java
// 	Builds a basic AWS Policy.
//===================================================================

package com.socialvagrancy.vail.commands.sub;

import java.lang.StringBuilder;
import java.util.ArrayList;

public class AWSPolicy
{
	public static String buildSimple(String version, String effect, String ARN, ArrayList<String> actions)
	{
		StringBuilder json = new StringBuilder();

		// JSON open
		json.append("{\n");

		// Version
		json.append("\t\"Version\":\"" + version + "\",\n");

		// Statement
		json.append("\t\"Statement\": [\n");
		json.append("\t\t{\n");

		// Sid
		json.append("\t\t\t\"Sid\": \"createdByVailSimpleJsonBuilder\",\n");

		// Effect
		
		json.append("\t\t\t\"Effect\": \"" + effect + "\",\n");

		// Action
		json.append("\t\t\t\"Action\": [\n");
		
		for(int i=0; i<actions.size(); i++)
		{
			json.append("\t\t\t\t\"" + actions.get(i) + "\",\n");
		}
		
		// Strip the last comma (and endline)
		json = json.deleteCharAt(json.length()-2);

		// Action close
		json.append("\t\t\t],\n");

		// Resource
		json.append("\t\t\t\"Resource\": \"" + ARN + "\"\n");

		// Statement close
		json.append("\t\t}\n");
		json.append("\t]\n");

		// JSON close
		json.append("}");

		return json.toString();
	}


}
