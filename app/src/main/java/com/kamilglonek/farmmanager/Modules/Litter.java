package com.kamilglonek.farmmanager.Modules;

import java.util.Date;

/**
 * Created by kamil on 02/22/18.
 */

public class Litter extends Animal {

    public int parentID;
    public int amount;
    public Date birthdate;

    public Litter(int animalID, int sowID, Date birthdate, int amount) {
        super(animalID);

        this.parentID = sowID;
        this.amount = amount;
        this.birthdate = birthdate;
    }
}
