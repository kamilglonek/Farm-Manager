package com.kamilglonek.farmmanager.Downloaders;

import com.kamilglonek.farmmanager.Modules.Sow;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kamil on 08/22/18.
 */

public class SowListDownloader {
    public ArrayList<Sow> loadSowList(String objectID) throws ParseException, JSONException {
        ArrayList<Sow> list = new ArrayList<>();
        ParseQuery sowList = new ParseQuery("sowList");
        ParseObject object = null;
        object = sowList.get(objectID);
        String jsonList = object.get("sows").toString();
        JSONArray array = null;
        array = new JSONArray(jsonList);

        for(int i =0; i < array.length(); i++) {
            JSONObject obj = null;
            obj = array.getJSONObject(i);
            list.add(new Sow(obj.get("sowID").toString()));
        }
        return list;
    }
}
