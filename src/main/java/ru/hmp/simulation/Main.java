package ru.hmp.simulation;

import ru.hmp.simulation.io.*;
import ru.hmp.simulation.iosim.ConsoleSimMapOutput;
import ru.hmp.simulation.map.SimulationMap;
import ru.hmp.simulation.map.TwoHashMapsSimulationMap;
import ru.hmp.simulation.model.*;
import ru.hmp.simulation.pathsearch.PathSearchAlgo;
import ru.hmp.simulation.pathsearch.aasterisk.AAsterisk;
import ru.hmp.simulation.simulation.BaseSimulation;
import ru.hmp.simulation.simulation.Simulation;
import ru.hmp.simulation.useractions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    private static final String PROP_FILE_NAME = "application";

    public static void main(String[] args) {
        init();
    }

    private static void init() {
        try {
            Output output = new ConsoleOutput();
            Input input = new ValidateInput(output, new ConsoleInput());

            PathSearchAlgo pathSearchAlgo = new AAsterisk();

            ConsoleSimMapOutput mapOutput = new ConsoleSimMapOutput(output);
            mapOutput.setConsoleSimMapOutput(prepareClassToCharMap());

            SimulationMap simulationMap = prepareSimMap();

            Simulation simulation = new BaseSimulation(simulationMap,
                    mapOutput,
                    prepareListOfEntity(simulationMap, pathSearchAlgo),
                    PropFileLoadHandler.getIntParam(PROP_FILE_NAME, "simulationRepetitionRate"),
                    PropFileLoadHandler.getIntParam(PROP_FILE_NAME, "renewableResourcesCycleLength"));

            String instruction = ResourceFilesLoadHandler.loadText("/help_text.txt");

            ArrayList<UserAction> actions = new ArrayList<>();
            actions.add(new StartSimulationAction(output, input, simulation));
            actions.add(new ResetSimulationMapAction(output, input, simulation));
            actions.add(new GetSimMapInfoAction(output, simulationMap));
            actions.add(new GetHelpAction(output, instruction));
            actions.add(new ExitConsoleAction(output));

            new Main().run(input, output, actions);

        } catch (Exception e) {
            System.out.println("Error occurred while running application");

            System.out.printf("Error description: %s%n", e.getMessage());
            if (e.getCause() != null && e.getCause().getMessage() != null) {
                System.out.printf("Error cause: %s%n", e.getCause().getMessage());
            }
            System.out.println("Application terminated");
        }
    }

    private static SimulationMap prepareSimMap() {
        int xMapSize = PropFileLoadHandler.getIntParam(PROP_FILE_NAME, "xMapSize");
        int yMapSize = PropFileLoadHandler.getIntParam(PROP_FILE_NAME, "yMapSize");
        if (xMapSize > 10_000) {
            throw new IllegalArgumentException("xMapSize parameter is set more than 10000");
        }
        if (yMapSize > 10_000) {
            throw new IllegalArgumentException("yMapSize parameter is set more than 10000");
        }
        return new TwoHashMapsSimulationMap(xMapSize, yMapSize);
    }

    private static List<Entity> prepareListOfEntity(SimulationMap simulationMap, PathSearchAlgo pathSearchAlgo) {
        int numOfPredators = PropFileLoadHandler.getIntParam(PROP_FILE_NAME, "numOfPredators");
        int numOfHerbivore = PropFileLoadHandler.getIntParam(PROP_FILE_NAME, "numOfHerbivore");
        int reproductionLimitOfGrassEatenForHerbivore =
                PropFileLoadHandler.getIntParam(PROP_FILE_NAME, "reproductionLimitOfGrassEatenForHerbivore");
        int percentageOfRocks = PropFileLoadHandler.getIntParam(PROP_FILE_NAME, "percentageOfRocks");
        int percentageOfGrasses = PropFileLoadHandler.getIntParam(PROP_FILE_NAME, "percentageOfGrasses");
        int percentageOfTrees = PropFileLoadHandler.getIntParam(PROP_FILE_NAME, "percentageOfTrees");

        List<Entity> listOfEntities = new ArrayList<>();
        int numOfPoints = simulationMap.getXMapSize() * simulationMap.getYMapSize();

        listOfEntities.addAll(EntityFactory.createNumOfEntities(EntityTypes.PREDATOR,
                simulationMap,
                pathSearchAlgo,
                0,
                numOfPredators));
        listOfEntities.addAll(EntityFactory.createNumOfEntities(EntityTypes.HERBIVORE,
                simulationMap,
                pathSearchAlgo,
                reproductionLimitOfGrassEatenForHerbivore,
                numOfHerbivore));

        listOfEntities.addAll(EntityFactory.createNumOfEntities(EntityTypes.GRASS,
                simulationMap,
                pathSearchAlgo,
                0,
                numOfPoints * percentageOfGrasses / 100));

        listOfEntities.addAll(EntityFactory.createNumOfEntities(EntityTypes.TREE,
                simulationMap,
                pathSearchAlgo,
                0,
                numOfPoints * percentageOfTrees / 100));

        listOfEntities.addAll(EntityFactory.createNumOfEntities(EntityTypes.ROCK,
                simulationMap,
                pathSearchAlgo,
                0,
                numOfPoints * percentageOfRocks / 100));
        return listOfEntities;
    }

    private static HashMap<Class, Character> prepareClassToCharMap() {
        char maskForPredator = PropFileLoadHandler.getCharParam(PROP_FILE_NAME, "maskForPredator");
        char maskForHerbivore = PropFileLoadHandler.getCharParam(PROP_FILE_NAME, "maskForHerbivore");
        char maskForRock = PropFileLoadHandler.getCharParam(PROP_FILE_NAME, "maskForRock");
        char maskForTree = PropFileLoadHandler.getCharParam(PROP_FILE_NAME, "maskForTree");
        char maskForGrass = PropFileLoadHandler.getCharParam(PROP_FILE_NAME, "maskForGrass");

        HashMap<Class, Character> mapClassToChar = new HashMap<>();

        mapClassToChar.put(Predator.class, maskForPredator);
        mapClassToChar.put(Herbivore.class, maskForHerbivore);
        mapClassToChar.put(Rock.class, maskForRock);
        mapClassToChar.put(Tree.class, maskForTree);
        mapClassToChar.put(Grass.class, maskForGrass);
        return mapClassToChar;
    }

    private void run(Input input, Output output, List<UserAction> actions) {
        boolean run = true;
        List<String> userActionNames = actions.stream().
                map(UserAction::name).
                collect(Collectors.toList());

        while (run) {
            int numOfMenuSelected = InputHandler.handleSelectItems(
                    input,
                    output,
                    System.lineSeparator() + "<<< Menu >>>",
                    userActionNames,
                    "Select action: ") - 1;
            UserAction action = actions.get(numOfMenuSelected);
            run = action.execute(input);
        }
    }
}