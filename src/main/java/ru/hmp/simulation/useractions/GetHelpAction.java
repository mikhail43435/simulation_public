package ru.hmp.simulation.useractions;

import ru.hmp.simulation.io.Input;
import ru.hmp.simulation.io.Output;

public final class GetHelpAction implements UserAction {

    private final Output out;
    private final String instruction;

    public GetHelpAction(Output out, String instruction) {
        this.out = out;
        this.instruction = instruction;
    }

    @Override
    public String name() {
        return "Get help";
    }

    @Override
    public boolean execute(Input input) {
        out.println(System.lineSeparator()
                + "Help:"
                + System.lineSeparator()
                + instruction);
        return true;
    }
}