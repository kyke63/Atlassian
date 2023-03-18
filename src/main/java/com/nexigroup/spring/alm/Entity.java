package com.nexigroup.spring.alm;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
	private Map<String, Field> fields = new HashMap();

	public Entity(String type) {
		this.type = type;
	}

	public void addField(Field field) {
		fields.put(field.getName().toLowerCase(), field);
	}

	public Field getField(String name) {
		return fields.get(name);
	}

	public void parseJson(JSONObject json) throws JSONException {
		type = json.getString("Type");
		JSONArray arr = (JSONArray) json.get("Fields");
		for (int i = 0; i < arr.length(); i++) {
			Field field = new Field();
//			System.out.println(arr.getJSONObject(i).toString(3));
			field.parseJson(arr.getJSONObject(i));
			addField(field);
		}

	}

}
