//============================= ActionTest ================================
// Description:
// This class represents a test action entity.
//
// Variables:
// - action: The action string.
// - necessary: Indicates if the action is necessary.
//
// Created by Sean Snyder.
//===========================================================================

package com.spectralogic.vail.vapir.model;

public class ActionTest {
    private String action;
    private boolean necessary;

    // Constructors
    public ActionTest() {
    }

    public ActionTest(String action, boolean necessary) {
        this.action = action;
        this.necessary = necessary;
    }
    //=============================
    // Getters
    //=============================

    public String getAction() { return action; }
    public boolean isNecessary() { return necessary; }


    //=============================
    // Setters
    //=============================

    public void setAction(String action) { this.action = action; }
    public void setNecessary(boolean necessary) { this.necessary = necessary; }
}
