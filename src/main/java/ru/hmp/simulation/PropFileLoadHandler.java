package ru.hmp.simulation;

import java.util.ResourceBundle;

public final class PropFileLoadHandler {

    private PropFileLoadHandler() {
    }

    public static String getStringParam(String propFileName, String paramName) {
        return ResourceBundle.getBundle(propFileName).getString(paramName);
    }

    public static int getIntParam(String propFileName, String paramName) {
        return Integer.parseInt(ResourceBundle.getBundle(propFileName).getString(paramName));
    }

    public static char getCharParam(String propFileName, String paramName) {
        return ResourceBundle.getBundle(propFileName).getString(paramName).charAt(0);
    }
}

