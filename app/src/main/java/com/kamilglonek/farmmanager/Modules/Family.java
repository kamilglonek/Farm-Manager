package com.kamilglonek.farmmanager.Modules;

import java.util.ArrayList;

/**
 * Created by kamil on 08/23/18.
 */

public class Family {

    public Sow sow;
    public ArrayList<Litter> litters = new ArrayList<>();
    public Family(Sow sow){
        this.sow = sow;
    }
}
