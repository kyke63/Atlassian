package com.nexigroup.spring.alm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Entities {

	private int totalResults ;
	Map<String,Entity> entities= new HashMap<>(); 


	public Entities(int totalResults) {
		this.totalResults = totalResults;
	}
	
	public void addEntity(Entity entity) {
		entities.put(entity.getField("id").getValue(0),entity);
	}
	
	public void parseJsonData(byte[] data) throws JSONException {
		JSONObject defects = new JSONObject(new String( data));
		totalResults = defects.getInt("TotalResults");
		JSONArray arr = (JSONArray) defects.get("entities");
		for(int i = 0 ; i < arr.length() ; i++){
			Entity ent = new Entity();
			ent.parseJson(arr.getJSONObject(i));
			addEntity(ent);
		}
		
	}
}
