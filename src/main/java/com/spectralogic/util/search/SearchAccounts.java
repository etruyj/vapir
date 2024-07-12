//===================================================================
// SearchAccounts.java
// 	Description:
// 		Tasks variable input and returns an account id
//===================================================================

package com.spectralogic.vail.vapir.util.search;

import com.spectralogic.vail.vapir.model.Account;
import com.spectralogic.vail.vapir.util.map.MapAccounts;
import java.util.HashMap;

public class SearchAccounts
{
	public static String findCanonicalId(Account[] accounts, String search)
	{
		HashMap<String, String> canon_id_map = MapAccounts.createIDCanonicalIDMap(accounts);
		HashMap<String, String> name_id_map = MapAccounts.createNameCanonicalIDMap(accounts);

		String id;

		if(canon_id_map.get(search) != null)
		{
			id = canon_id_map.get(search);
		}
		else if(name_id_map.get(search) != null)
		{
			id = name_id_map.get(search);
		}
		else
		{
			id = "none";
		}

		return id;
	}
	
    public static String findId(Account[] accounts, String search)
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

