package ru.hmp.simulation.useractions;

import ru.hmp.simulation.io.Input;
import ru.hmp.simulation.io.InputHandler;
import ru.hmp.simulation.io.Output;
import ru.hmp.simulation.simulation.Simulation;

public final class ResetSimulationMapAction implements UserAction {

    private final Output output;
    private final Input input;
    private final Simulation simulation;

    public ResetSimulationMapAction(Output output, Input input, Simulation simulation) {
        this.output = output;
        this.input = input;
        this.simulation = simulation;
    }

    @Override
    public String name() {
        return "Reset simulation";
    }

    @Override
    public boolean execute(Input input) {

        char charIn = InputHandler.handleSingleCharInput(input,
                output,
                "Are you sure to reset simulation [y/n]: ");

        if (charIn == 'y' || charIn == 'Y') {
            simulation.resetSimulation();
            output.println("Simulation has been reset");
        } else if (charIn == 'n' || charIn == 'N') {
            output.println("Simulation reset canceled");
        } else {
            output.println("Invalid input");
        }
        return true;
    }
}