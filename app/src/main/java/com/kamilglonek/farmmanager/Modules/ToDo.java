package com.kamilglonek.farmmanager.Modules;

/**
 * Created by kamil on 08/23/18.
 */

public class ToDo {

    public String taskName;
    public String taskDate;
    public String parentID;

    public ToDo(String taskName, String taskDate, String parentID) {

        this.taskName = taskName;
        this.taskDate = taskDate;
        this.parentID = parentID;
    }
}