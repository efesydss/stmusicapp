package com.st.stmusic.data;

import java.io.Serializable;

public class CurrentModel implements Serializable {
    private String currentID;

    public String getCurrentID() {
        return currentID;
    }

    public void setCurrentID(String currentID) {
        this.currentID = currentID;
    }

    public CurrentModel(String currentID) {
        this.currentID = currentID;
    }
}
