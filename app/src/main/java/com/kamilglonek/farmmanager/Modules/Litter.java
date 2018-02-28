package com.kamilglonek.farmmanager.Modules;

import java.util.Date;

/**
 * Created by kamil on 02/22/18.
 */

public class Litter extends Animal {

    public int amount;
    public Date birthdate;
    public int parentID;
    public Litter(int animalID, Date birthdatte, int amount, int sowID) {
        super(animalID);

        this.amount = amount;
        this.birthdate = birthdatte;
        this.parentID = sowID;
    }
}
