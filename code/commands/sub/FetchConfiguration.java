//===================================================================
// FetchConfiguration.java
// 	Description:
// 		Queries the Vail Sphere for configuration specific
// 		information and outputs it to the shell in json format.
//===================================================================

package com.socialvagrancy.vail.commands.sub;

import com.socialvagrancy.vail.commands.BasicCommands;
import com.socialvagrancy.vail.structures.SphereConfig;

public class FetchConfiguration
{
	public static SphereConfig fromSphere(BasicCommands sphere, String ip_address)
	{
		SphereConfig config = new SphereConfig();
		config.accounts = sphere.listAccounts(ip_address);
		config.buckets = sphere.listBuckets(ip_address);
		config.lifecycles = sphere.listLifecycles(ip_address);
		config.storage = sphere.listStorage(ip_address);

		return config;
	}
}
