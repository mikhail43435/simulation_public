package ru.hmp.simulation.render.console;

import ru.hmp.simulation.exceptions.MapperClassToCharNotSetException;
import ru.hmp.simulation.io.Output;
import ru.hmp.simulation.map.Position;
import ru.hmp.simulation.map.SimulationMap;
import ru.hmp.simulation.model.Entity;
import ru.hmp.simulation.render.SimMapRenderer;

import java.util.Map;

public class ConsoleSimMapRenderer implements SimMapRenderer {

    private final Output output;
    private final SimulationMap simulationMap;
    private Map<Class<? extends Entity>, Character> mapperClassToChar;

    public ConsoleSimMapRenderer(Output output, SimulationMap simulationMap) {
        this.output = output;
        this.simulationMap = simulationMap;
    }

    @Override
    public void renderMap() {

        if (mapperClassToChar == null) {
            throw new MapperClassToCharNotSetException("Mapper for class ConsoleSimMapOutput has not been set");
        }

        char[][] map = new char[simulationMap.getYMapSize()][simulationMap.getXMapSize()];
        for (Entity entity : simulationMap.getListOfEntities()) {
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

    public void setEntityMapper(Map<Class<? extends Entity>, Character> mapperClassToChar) {
        this.mapperClassToChar = mapperClassToChar;
    }
}