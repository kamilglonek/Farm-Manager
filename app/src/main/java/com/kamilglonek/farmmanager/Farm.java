package com.kamilglonek.farmmanager;

/**
 * Created by kamil on 29.11.2017.
 */

public class Farm {

    private String farmName;
    private String farmOwner;
    private String animalType;

    public Farm(String farmName, String farmOwner, String animalType){
        this.farmName = farmName;
        this.farmOwner = farmOwner;
        this.animalType = animalType;
    }

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
