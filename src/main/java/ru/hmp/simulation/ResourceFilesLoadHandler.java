package ru.hmp.simulation;

import ru.hmp.simulation.exceptions.LoadResourcesException;

import java.nio.file.Files;
import java.nio.file.Paths;

public final class ResourceFilesLoadHandler {

    private ResourceFilesLoadHandler() {
    }

    public static String loadText(String resourceFileName) {
        String string;
        try {
            string = Files.readString(
                    Paths.get(ResourceFilesLoadHandler.class.getResource(resourceFileName).toURI()));
        } catch (Exception e) {
            throw new LoadResourcesException("Internal error while load data", e);
        }
        return string;
    }
}