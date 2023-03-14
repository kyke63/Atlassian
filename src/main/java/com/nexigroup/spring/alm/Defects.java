package com.nexigroup.spring.alm;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.nexigroup.spring.alm.infrastructure.Response;
import com.nexigroup.spring.alm.infrastructure.RestConnectorALM;

public class Defects {
	private RestConnectorALM con;

	public Defects() {
		con = RestConnectorALM.getInstance();
	}

	public Entities getDefects() throws Exception {
		return getDefects(null);
	}

	public Entities getDefects(String query) throws Exception {
		String defectsUrl = con.buildEntityCollectionUrl("defect");
		System.out.println(defectsUrl);
		Map<String, String> map = new HashMap<String, String>();
		map.put("Content-Type", "application/json");
		map.put("Accept", "application/json");
		String queryString = "start-index=1&page-size=5";
		if (query != null && !query.isEmpty())
			queryString = "query=" + URLEncoder.encode(query, "UTF-8");

		Response response = con.httpGet(defectsUrl, queryString, map);
		Entities defects = new Entities();
		defects.parseJsonData(response.getResponseData());

		return defects;

	}

}
