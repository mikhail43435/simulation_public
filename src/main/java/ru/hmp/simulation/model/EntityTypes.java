package ru.hmp.simulation.model;

import ru.hmp.simulation.model.entities.*;

public enum EntityTypes {
    PREDATOR("Predator class", Predator.class),
    HERBIVORE("Herbivore class", Herbivore.class),
    GRASS("Grass class", Grass.class),
    TREE("Tree class", Tree.class),
    ROCK("Rock class", Rock.class);

    private final String className;
    private final Class<? extends Entity> classType;

    EntityTypes(String name, Class<? extends Entity> classType) {
        this.className = name;
        this.classType = classType;
    }

    public String getName() {
        return className;
    }

    public Class<? extends Entity> getClassType() {
        return classType;
    }

}
