package edu.neu.madcourse.numad21fa_a7team21days.utils;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

public class EventParamUtil {

    private static final String MOMENTO = "momento";


    public static JSONObject getJsonObjectFromMap(Map<String, String> params) {
        JSONObject eventProperties = new JSONObject();
        Iterator<Map.Entry<String, String>> entries = params.entrySet().iterator();
        try {
            while (entries.hasNext()) {
                Map.Entry<String, String> entry = entries.next();
                eventProperties.put(entry.getKey(), entry.getValue());
            }
        } catch (JSONException exception) {

        }
        return eventProperties;
    }

    public static Bundle getBundleFromMap(Map<String, String> params) {
        Bundle bundle = new Bundle();
        Iterator<Map.Entry<String, String>> entries = params.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            bundle.putString(entry.getKey(), entry.getValue());
        }
        return bundle;
    }
}
