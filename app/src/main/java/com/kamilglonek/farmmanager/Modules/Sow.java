package com.kamilglonek.farmmanager.Modules;

/**
 * Created by kamil on 02/22/18.
 */

public class Sow extends Animal {

    public int sowID;

    public Sow(int animalID, int sowID) {
        super(animalID);

        this.sowID = sowID;
    }
}
