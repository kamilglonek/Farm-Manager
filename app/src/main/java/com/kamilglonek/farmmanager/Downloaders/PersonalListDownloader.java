package com.kamilglonek.farmmanager.Downloaders;

import com.kamilglonek.farmmanager.Structures.ListItem;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kamil on 05/13/18.
 */

public class PersonalListDownloader {

  public ArrayList<ListItem> loadList(String objectID) throws JSONException {
        ArrayList<ListItem> list = new ArrayList<>();
        ParseQuery personalList = new ParseQuery("personalList");
        ParseObject object = null;
        try {
            object = personalList.get(objectID);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String jsonList = object.get("list").toString();
        JSONArray array = null;
        array = new JSONArray(jsonList);

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = null;
            try {
                obj = array.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                list.add(new ListItem(obj.get("dayNumber").toString(), obj.get("taskName").toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}

