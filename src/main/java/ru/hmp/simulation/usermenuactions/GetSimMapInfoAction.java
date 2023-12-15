package ru.hmp.simulation.usermenuactions;

import ru.hmp.simulation.io.Input;
import ru.hmp.simulation.io.Output;
import ru.hmp.simulation.map.SimulationMap;

public final class GetSimMapInfoAction implements UserAction {

    private final Output output;
    private final SimulationMap simulationMap;

    public GetSimMapInfoAction(Output output, SimulationMap simulationMap) {
        this.output = output;
        this.simulationMap = simulationMap;
    }

    @Override
    public String name() {
        return "Get simulation map info";
    }

    @Override
    public boolean execute(Input input) {
        output.printLineSeparator();
        output.println("=== Simulation map info ===");
        output.println(getSimMapStatistic());
        return true;
    }

    private String getSimMapStatistic() {
        StringBuilder sb = new StringBuilder(550);
        simulationMap.getStatistic().
                forEach((key, value) -> sb.
                        append(key).
                        append(": ").
                        append(value).
                        append(System.lineSeparator()));
        return sb.toString();
    }
}