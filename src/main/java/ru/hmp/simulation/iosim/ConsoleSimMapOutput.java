package ru.hmp.simulation.iosim;

import ru.hmp.simulation.exceptions.MapperClassToCharNotSetException;
import ru.hmp.simulation.io.Output;
import ru.hmp.simulation.map.Position;
import ru.hmp.simulation.map.SimulationMap;
import ru.hmp.simulation.model.Entity;

import java.util.HashMap;

public class ConsoleSimMapOutput implements SimMapOutput {

    private final Output output;
    private HashMap<Class, Character> mapperClassToChar;

    public ConsoleSimMapOutput(Output output) {
        this.output = output;
    }

    @Override
    public void displayMap(SimulationMap simulationMap) {

        if (mapperClassToChar == null) {
            throw new MapperClassToCharNotSetException("Mapper for class ConsoleSimMapOutput has not been set");
        }

        char[][] map = new char[simulationMap.getYMapSize()][simulationMap.getXMapSize()];
        for (Entity entity : simulationMap.getListOfEntityLeft()) {
            Position entityPosition = simulationMap.getEntityPosition(entity);

            map[entityPosition.getY()][entityPosition.getX()] = mapperClassToChar.get(entity.getClass());
        }

        output.println("Cycle # " + simulationMap.getCycleNumber());
        output.println("=".repeat(simulationMap.getXMapSize() + 4));
        for (char[] arr : map) {
            output.print("||");
            for (char ch : arr) {
                if (ch == 0) {
                    ch = ' ';
                }
                output.print(ch);
            }
            output.println("||");
        }
        output.println("=".repeat(simulationMap.getXMapSize() + 4));
    }

    public void setConsoleSimMapOutput(HashMap<Class, Character> mapperClassToChar) {
        this.mapperClassToChar = mapperClassToChar;
    }
}