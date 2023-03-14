package com.nexigroup.spring.jira;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.client.RestClientException;

import com.nexigroup.spring.jira.infrastructure.Response;
import com.nexigroup.spring.jira.infrastructure.RestConnectorJIRA;

public class SearchClient {

	private RestConnectorJIRA con;

	private static final String START_AT_ATTRIBUTE = "startAt";
	private static final String MAX_RESULTS_ATTRIBUTE = "maxResults";
	private static final int MAX_JQL_LENGTH_FOR_HTTP_GET = 500;
	private static final String JQL_ATTRIBUTE = "jql";
	private static final String FILTER_FAVOURITE_PATH = "filter/favourite";
	private static final String FILTER_PATH_FORMAT = "filter/%s";
	private static final String SEARCH_URI_PREFIX = "search";
	private static final String EXPAND_ATTRIBUTE = "expand";
	private static final String FIELDS_ATTRIBUTE = "fields";

	public SearchClient() {
		con = RestConnectorJIRA.getInstance();
	}

	/**
	 * Performs a JQL search and returns issues matching the query
	 *
	 * @param jql a valid JQL query (will be properly encoded by JIRA client).
	 *            Restricted JQL characters (like '/') must be properly escaped.
	 * @return issues matching given JQL query
	 * @throws Exception
	 * @throws RestClientException in case of problems (connectivity, malformed
	 *                             messages, invalid JQL query, etc.)
	 */
	public String searchJql(String jql, Integer maxResults, Integer startAt) throws Exception {
		Map<String, Map<String, String>> customField = getCustomFields();
		String defectsUrl = con.buildUrl("rest/api/latest/" + SEARCH_URI_PREFIX);
		Map<String, String> map = new HashMap<String, String>();
		map.put("Content-Type", "application/json");
		map.put("Accept", "application/json");
		JSONObject json = new JSONObject();
		if (jql != null && !jql.isEmpty())
			json.put(JQL_ATTRIBUTE, jql);
		Response response = con.httpPost(defectsUrl, json.toString().getBytes(), map);
		JSONObject result = new JSONObject(new String(response.getResponseData()));
		JSONArray issues = result.getJSONArray("issues");
		for (int i = 0; i < issues.length(); i++) {
			JSONObject issue = issues.getJSONObject(i);
			System.out.println(issue.toString());
			JSONObject fields = issue.getJSONObject("fields");
			System.out.println();
			System.out.println();
			System.out.println();
			for (Object name : fields.toMap().keySet()) {
				String realname = name.toString();
				if (realname.startsWith("customfield_")) {
					realname = customField.get(realname).get("name");
				}
				Object value = fields.get(name.toString());
				if (!("progress".equals(realname) || "votes".equals(realname) || "watches".equals(realname)
						|| "aggregateprogress".equals(realname) || "Development".equals(realname))) {
					if (value instanceof JSONArray) {
						if (((JSONArray) value).length() > 0) {
							value = ((JSONArray) value).getJSONObject(0).getString("value");
						} else {
							// System.out.println(((JSONArray) value).toString(1));
							value = "";
						}
					} else if (value instanceof JSONObject) {
						if (((JSONObject) value).toMap().containsKey("value")) {
							value = ((JSONObject) value).getString("value");
						} else if (((JSONObject) value).toMap().containsKey("name")) {
							value = ((JSONObject) value).getString("name");
						} else {
							System.out.println(((JSONObject) value).toString(1));
						}
					}
					System.out.println("'" + realname + "' <--> '" + value + "'");
				}
			}
			System.out.println();
			System.out.println();
			System.out.println();
		}
		return "";
	}

	public Map<String, Map<String, String>> getCustomFields() throws Exception {
		String defectsUrl = con.buildUrl("rest/api/latest/customFields");
		Map<String, String> map = new HashMap<String, String>();
		map.put("Content-Type", "application/json");
		map.put("Accept", "application/json");
		Boolean isLast = false;
		Integer startAt = 1;
		Map<String, Map<String, String>> fieldsMap = new HashMap<>();
		while (!isLast) {
			String url = defectsUrl + "?startAt=" + startAt;
			Response response = con.httpGet(url, null, map);
			JSONObject result = new JSONObject(new String(response.getResponseData()));
			JSONArray fields = result.getJSONArray("values");
			isLast = result.getBoolean("isLast");
			startAt++;
			for (int i = 0; i < fields.length(); i++) {
				JSONObject field = fields.getJSONObject(i);
				Map<String, String> fieldMap = new HashMap<>();
				fieldMap.put("id", field.getString("id"));
				fieldMap.put("name", field.getString("name"));
				fieldMap.put("description", field.getString("description"));
				fieldsMap.put(field.getString("id"), fieldMap);
			}
		}

		return fieldsMap;
	}

	/**
	 * Performs a JQL search and returns issues matching the query. The first
	 * startAt issues will be skipped and SearchResult will contain at most
	 * maxResults issues. List of issue fields which should be included in the
	 * result may be specified.
	 *
	 * @param jql        a valid JQL query (will be properly encoded by JIRA
	 *                   client). Restricted JQL characters (like '/') must be
	 *                   properly escaped. All issues matches to the null or empty
	 *                   JQL.
	 * @param maxResults maximum results for this search. When null is given, the
	 *                   default maxResults configured in JIRA is used (usually 50).
	 * @param startAt    starting index (0-based) defining how many issues should be
	 *                   skipped in the results. For example for startAt=5 and
	 *                   maxResults=3 the results will include matching issues with
	 *                   index 5, 6 and 7. For startAt = 0 and maxResults=3 the
	 *                   issues returned are from position 0, 1 and 2. When null is
	 *                   given, the default startAt is used (0).
	 * @param fields     set of fields which should be retrieved. You can specify
	 *                   *all for all fields or *navigable (which is the default
	 *                   value, used when null is given) which will cause to include
	 *                   only navigable fields in the result. To ignore the specific
	 *                   field you can use "-" before the field's name. Note that
	 *                   the following fields: summary, issuetype, created, updated,
	 *                   project and status are required. These fields are included
	 *                   in *all and *navigable.
	 * @return issues matching given JQL query
	 * @throws RestClientException in case of problems (connectivity, malformed
	 *                             messages, invalid JQL query, etc.)
	 */
//    public SearchResult searchJql(@Nullable String jql, @Nullable Integer maxResults, @Nullable Integer startAt, @Nullable Set<String> fields);

	/**
	 * Retrieves list of your favourite filters.
	 *
	 * @return list of your favourite filters
	 * @since 2.0 client, 5.0 server
	 */
//    public Iterable<Filter> getFavouriteFilters();

	/**
	 * Retrieves filter for given URI.
	 *
	 * @param filterUri URI to filter resource (usually get from <code>self</code>
	 *                  attribute describing component elsewhere)
	 * @return filter
	 * @since 2.0 client, 5.0 server
	 */
//    public Filter getFilter(URI filterUri);

	/**
	 * Retrieves filter for given id.
	 *
	 * @param id ID of the filter
	 * @return filter
	 * @since 2.0 client, 5.0 server
	 */
//    public Filter getFilter(long id);

}
