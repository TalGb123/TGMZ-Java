package com.example.myapplication.model;

import java.io.Serializable;

/**
 * Represents the summarized build data saved inside the User's profile array.
 */
public class SavedBuild implements Serializable {
    private String buildRef; // The Firestore Document ID of the full build
    private String buildName;
    private long savedAt;

    public SavedBuild() {}

    public SavedBuild(String buildRef, String buildName, long savedAt) {
        this.buildRef = buildRef;
        this.buildName = buildName;
        this.savedAt = savedAt;
    }

    public String getBuildRef() { return buildRef; }
    public void setBuildRef(String buildRef) { this.buildRef = buildRef; }

    public String getBuildName() { return buildName; }
    public void setBuildName(String buildName) { this.buildName = buildName; }

    public long getSavedAt() { return savedAt; }
    public void setSavedAt(long savedAt) { this.savedAt = savedAt; }
}