package com.kamilglonek.farmmanager;

import org.json.JSONArray;

/**
 * Created by kamil on 29.11.2017.
 */

public class Farm {

    private String farmName;
    private String farmOwner;
    private String animalType;

    public JSONArray animals = new JSONArray();

    public Farm(String farmName, String farmOwner, String animalType){
        this.farmName = farmName;
        this.farmOwner = farmOwner;
        this.animalType = animalType;
    }

//    public void addAnimal(View view, String animalID, String birthDate, String parent){
//
//        try{
//            animals.put(new JSONObject().put("animalID", animalID).put("birthdate", birthDate).put("Parent", parent));
//        }catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    ParseObject parseAnimal = new ParseObject("Animal");


    public String getFarmName() {
        return farmName;
    }

    public String getFarmOwner() {
        return farmOwner;
    }

    public String getAnimalType () {
        return animalType;
    }



}
