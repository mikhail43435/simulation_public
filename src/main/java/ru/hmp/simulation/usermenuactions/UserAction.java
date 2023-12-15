package ru.hmp.simulation.usermenuactions;

import ru.hmp.simulation.io.Input;

public interface UserAction {
    String name();

    boolean execute(Input input);
}