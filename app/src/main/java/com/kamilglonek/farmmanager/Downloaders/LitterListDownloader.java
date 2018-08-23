package com.kamilglonek.farmmanager.Downloaders;

import com.kamilglonek.farmmanager.Modules.Litter;
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

public class LitterListDownloader {
    public ArrayList<Litter> loadLitterList(String objectID) throws ParseException, JSONException {
        ArrayList<Litter> list = new ArrayList<>();
        ParseQuery litterList = new ParseQuery("litterList");
        ParseObject object = null;
        object = litterList.get(objectID);
        String jsonList = object.get("litters").toString();
        JSONArray array = null;
        array = new JSONArray(jsonList);

        for(int i =0; i < array.length(); i++) {
            JSONObject obj = null;
            obj = array.getJSONObject(i);
            String parentID = (String) obj.get("parentID");
            String birthdate = (String) obj.get("birthdate");
            int amount = (int) obj.get("amount");
            list.add(new Litter(parentID, birthdate, amount));
        }
        return list;
    }
}
