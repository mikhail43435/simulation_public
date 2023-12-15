package ru.hmp.simulation.util;

import ru.hmp.simulation.exceptions.LoadResourcesException;

import javax.swing.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class ResourcesLoadHandler {

    private ResourcesLoadHandler() {
    }

    public static String loadText(String resourceFileName) {
        String string;
        try {
            string = Files.readString(
                    Paths.get(ResourcesLoadHandler.class.getResource(resourceFileName).toURI()));
        } catch (Exception e) {
            throw new LoadResourcesException("Internal error while load data", e);
        }
        return string;
    }

    public static ImageIcon loadImageIcon(String imageFilePathName) {
        ImageIcon imageIcon;
        try {
            imageIcon = new ImageIcon(ResourcesLoadHandler.class.getResource(imageFilePathName));
        } catch (Exception e) {
            throw new LoadResourcesException("Internal error while load data", e);
        }
        return imageIcon;
    }
}