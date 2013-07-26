package com.mr1964.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {
	
	private String json = null;
	
	public JsonParser(String json){
		this.json = json;
	}
	
	public String getString(String item,int i) throws JSONException{
		JSONArray jsonArray = new JSONArray(json);
		JSONObject obj = jsonArray.getJSONObject(i);
		String str = obj.getString(item);
		return str;
	}
	
	
}
