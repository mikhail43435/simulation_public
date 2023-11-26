package ru.hmp.simulation.useractions;

import ru.hmp.simulation.io.Input;

public interface UserAction {
    String name();

    boolean execute(Input input);
}