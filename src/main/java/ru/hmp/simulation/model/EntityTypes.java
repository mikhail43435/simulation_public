package ru.hmp.simulation.model;

public enum EntityTypes {
    PREDATOR("Predator class", Predator.class),
    HERBIVORE("Herbivore class", Herbivore.class),
    GRASS("Grass class", Grass.class),
    TREE("Tree class", Tree.class),
    ROCK("Rock class", Rock.class);

    private final String className;
    private final Class classType;

    EntityTypes(String name, Class classType) {
        this.className = name;
        this.classType = classType;
    }

    public String getName() {
        return className;
    }

    public Class getClassType() {
        return classType;
    }

}
