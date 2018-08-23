package com.kamilglonek.farmmanager.Modules;

/**
 * Created by kamil on 02/22/18.
 */

public class Litter {

    public String parentID;
    public int amount;
    public String birthdate;

    public Litter(String sowID, String birthdate, int amount) {
        this.parentID = sowID;
        this.amount = amount;
        this.birthdate = birthdate;
    }
}
