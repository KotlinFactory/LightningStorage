package de.leonhard.storage.util;

import java.util.*;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings({"unchecked", "WeakerAccess"})
@UtilityClass
public class JsonUtils {

  public Map<String, Object> jsonToMap(final JSONObject jsonObject) throws JSONException {
    Map<String, Object> retMap = new HashMap<>();

    if (jsonObject != JSONObject.NULL) {
      retMap = toMap(jsonObject);
    }
    return retMap;
  }

  public JSONObject getJsonFromMap(final Map<String, Object> map) throws JSONException {
    final JSONObject jsonData = new JSONObject();

    for (val entry : map.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();
      if (value instanceof Map) {
        value = getJsonFromMap((Map<String, Object>) value);
      }
      jsonData.put(key, value);
    }
    return jsonData;
  }

  public Map<String, Object> toMap(final JSONObject jsonObject) throws JSONException {
    final Map<String, Object> map = new HashMap<>();

    final Iterator<String> keysItr = jsonObject.keys();
    keysItr.forEachRemaining(key -> map.put(key, getValue(jsonObject.get(key))));
    return map;
  }

  public List<Object> toList(final JSONArray array) throws JSONException {
    final List<Object> list = new ArrayList<>();
    for (int i = 0; i < array.length(); i++) {
      list.add(getValue(array.get(i)));
    }
    return list;
  }

  private Object getValue(final Object obj) {
    if (obj instanceof JSONArray) {
      return toList((JSONArray) obj);
    } else if (obj instanceof JSONObject) {
      return toMap((JSONObject) obj);
    } else {
      return obj;
    }
  }
}
