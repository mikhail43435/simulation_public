package ru.hmp.simulation.usermenuactions;


import ru.hmp.simulation.io.Input;
import ru.hmp.simulation.io.Output;

public final class ExitConsoleAction implements UserAction {
    private final Output out;

    public ExitConsoleAction(Output out) {
        this.out = out;
    }

    @Override
    public String name() {
        return "Close application";
    }

    @Override
    public boolean execute(Input input) {
        out.println("Application is closed");
        return false;
    }
}

