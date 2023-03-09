package com.nexigroup.spring.alm;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor()
@ToString
public class Field {

	private String name;
	private List<Value> values = new ArrayList<>();

	public Field(String name) {
		this.name = name;
	}

	public void addValue(Value value) {
		values.add(value);
	}

	public String getValue(int i) {
		return values.get(i).getValue();
	}

	public void parseJson(JSONObject json) throws JSONException {
		name = json.getString("Name");
		JSONArray arr = (JSONArray) json.get("values");
		for (int i = 0; i < arr.length(); i++) {
			Value value = new Value();
			if (arr.get(i) instanceof JSONObject) {
				if ("{}".equals(arr.getJSONObject(i).toString()))
					value.setValue("");
				else
					value.setValue(arr.getJSONObject(i).getString("value"));
			} else {
				value.setValue(arr.getString(i));
			}
			addValue(value);
		}
	}

}
