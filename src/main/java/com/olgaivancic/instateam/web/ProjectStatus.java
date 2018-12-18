package com.olgaivancic.instateam.web;

public enum ProjectStatus {
    NOTSTARTED("Not Started", "#dede2a"),
    ACTIVE("Active", "#72c38d"),
    ARCHIVED("Archived", "#bbbab9");

    private final String name;
    private final String hexCode;

    ProjectStatus(String name, String hexCode) {
        this.name = name;
        this.hexCode = hexCode;
    }

    public String getName() {
        return name;
    }

    public String getHexCode() {
        return hexCode;
    }

}
