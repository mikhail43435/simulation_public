package ru.hmp.simulation.model;

public abstract class StaticEntity extends Entity {

    @Override
    public String toString() {
        return "StaticEntity{" +
                "class=" + this.getClass() +
                "hashCode=" + this.hashCode() +
                '}';
    }
}