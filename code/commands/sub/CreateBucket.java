//===================================================================
// CreateBucket.java
//	Description: 
//		For co 
//==================================================================

package com.socialvagrancy.vail.commands.sub;

import com.socialvagrancy.vail.commands.BasicCommands;
import com.socialvagrancy.vail.structures.Bucket;
import com.socialvagrancy.utils.Logger;

import com.google.gson.Gson;

public class CreateBucket
{
	public static Bucket createWithJson(BasicCommands sphere, String ip_address, Bucket bucket, Logger logbook)
	{
		Gson gson = new Gson();

		logbook.logWithSizedLogRotation("Creating bucket [" + bucket.name + "] for account [" + bucket.owner + "]...", 1);

		String json_body = gson.toJson(bucket);
		
		return sphere.createBucket(ip_address, bucket.name, json_body);
	}

}
