package com.kamilglonek.farmmanager.Modules;

/**
 * Created by kamil on 08/23/18.
 */

public class Task {

    public ToDo toDo;
    public int daysLeft;

    public Task(ToDo toDo, int daysLeft) {
        this.toDo = toDo;
        this.daysLeft = daysLeft;
    }
}
