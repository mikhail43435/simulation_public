package ru.hmp.simulation.useractions;

import ru.hmp.simulation.exceptions.IdleSimulationException;
import ru.hmp.simulation.exceptions.NoFreeSpaceOnMapException;
import ru.hmp.simulation.io.Input;
import ru.hmp.simulation.io.InputHandler;
import ru.hmp.simulation.io.Output;
import ru.hmp.simulation.simulation.Simulation;

public final class StartSimulationAction implements UserAction {

    private final Output output;
    private final Input input;
    private final Simulation simulation;

    public StartSimulationAction(Output output, Input input, Simulation simulation) {
        this.output = output;
        this.input = input;
        this.simulation = simulation;
    }

    @Override
    public String name() {
        return "Start (continue) simulation";
    }

    @Override
    public boolean execute(Input input) {

        int numberOfSimulationCycles = InputHandler.handleSingleIntInput(input,
                output,
                "Enter number of simulation cycles (1 - 1000): ",
                1,
                1000);
        try {
            simulation.runSimulation(numberOfSimulationCycles);
        } catch (IdleSimulationException | NoFreeSpaceOnMapException e) {
            if (e.getClass() == NoFreeSpaceOnMapException.class) {
                output.println("Simulation map overflowing occurred.");
            } else {
                output.println(e.getMessage());
            }
            output.println("Simulation was stopped. Map is reset.");
            simulation.resetSimulation();
        } catch (InterruptedException e) {
            throw new IllegalStateException();
        }
        return true;
    }
}