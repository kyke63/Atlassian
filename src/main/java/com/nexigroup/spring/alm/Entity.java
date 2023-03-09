package com.nexigroup.spring.alm;

import java.util.HashMap;
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
public class Entity {
	
	private String type;  
	private Map<String,Field> fields = new HashMap();
	
	public Entity(String type) {
		this.type = type;
	}
	
	public void addField(Field field) {
		fields.put(field.getName(), field);
	}

	public Field getField(String name) {
		return fields.get(name);
	}
	
	public void parseJson(JSONObject json) throws JSONException {
		type = json.getString("Type");
		JSONArray arr = (JSONArray)json.get("Fields");
		for(int i = 0 ; i < arr.length() ; i++){
			Field field = new Field();
			field.parseJson(arr.getJSONObject(i));
			addField(field);
		}
		
	}


}
