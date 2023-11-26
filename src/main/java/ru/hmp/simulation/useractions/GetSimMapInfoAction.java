package ru.hmp.simulation.useractions;

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
        output.println("=== Simulation map info ===");
        output.println(String.format("x size: %d", simulationMap.getXMapSize()));
        output.println(String.format("у size: %d", simulationMap.getYMapSize()));
        output.println(String.format("number of points: %d", simulationMap.getXMapSize() * simulationMap.getYMapSize()));
        output.println(String.format("cycle counter: %d", simulationMap.getCycleNumber()));
        output.println(String.format("num of entities on the map: %d", simulationMap.getListOfEntityLeft().size()));
        output.println("=== End of info ===");
        return true;
    }
}