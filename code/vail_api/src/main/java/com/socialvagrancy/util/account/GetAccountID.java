//===================================================================
// GetAccountID.java
// 	Description:
// 		Tasks variable input and returns an account id
//===================================================================

package com.socialvagrancy.vail.utils.account;

import com.socialvagrancy.vail.structures.Account;
import com.socialvagrancy.vail.utils.map.MapAccounts;
import java.util.HashMap;

public class GetAccountID
{
	public static String findAccount(Account[] accounts, String search)
	{
		HashMap<String, String> canon_id_map = MapAccounts.createCanonicalIDMap(accounts);
		HashMap<String, String> name_id_map = MapAccounts.createNameIDMap(accounts);
		HashMap<String, String> id_name_map = MapAccounts.createIDNameMap(accounts);

		String id;

		if(id_name_map.get(search) != null)
		{
			id = canon_id_map.get(search);
		}
		else if(name_id_map.get(search) != null)
		{
			id = name_id_map.get(search);
		}
		else if(canon_id_map.get(search) != null)
		{
			id = canon_id_map.get(search);
		}
		else
		{
			id = "none";
		}

		return id;
	}
}
