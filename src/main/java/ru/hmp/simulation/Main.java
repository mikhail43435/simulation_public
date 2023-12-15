package ru.hmp.simulation;

import ru.hmp.simulation.io.*;
import ru.hmp.simulation.map.SimulationMap;
import ru.hmp.simulation.map.TwoHashMapsSimulationMap;
import ru.hmp.simulation.model.Entity;
import ru.hmp.simulation.model.EntityTypes;
import ru.hmp.simulation.model.entities.*;
import ru.hmp.simulation.pathsearch.PathSearchAlgo;
import ru.hmp.simulation.pathsearch.aasterisk.AAsterisk;
import ru.hmp.simulation.render.SimMapRenderer;
import ru.hmp.simulation.render.console.ConsoleSimMapRenderer;
import ru.hmp.simulation.render.swing.SwingSimMapRenderer;
import ru.hmp.simulation.simulation.BaseSimulation;
import ru.hmp.simulation.usermenuactions.*;
import ru.hmp.simulation.util.PropFileLoadHandler;
import ru.hmp.simulation.util.ResourcesLoadHandler;

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
//
//        try {
        PathSearchAlgo pathSearchAlgo = new AAsterisk();
        SimulationMap simulationMap =
                initSimMap();

        BaseSimulation simulation = new BaseSimulation(simulationMap,
                initListOfEntities(simulationMap, pathSearchAlgo),
                PropFileLoadHandler.getIntParam(PROP_FILE_NAME, "simulationRepetitionRate"),
                PropFileLoadHandler.getIntParam(PROP_FILE_NAME, "renewableResourcesCycleLength"));

        String instructionText = ResourcesLoadHandler.loadText("/help_text.txt");

        int outputMode = PropFileLoadHandler.getIntParam(PROP_FILE_NAME, "wayToDisplay");

        if (outputMode == 1) {
            Output output = new ConsoleOutput();
            Input input = new ValidateInput(output, new ConsoleInput());

            ConsoleSimMapRenderer consoleSimMapRenderer = new ConsoleSimMapRenderer(output, simulationMap);
            consoleSimMapRenderer.setEntityMapper(initEntitiesMapper());
            simulation.setSimulationMapRenderer(consoleSimMapRenderer);

            ArrayList<UserAction> actions = new ArrayList<>();

            actions.add(new StartSimulationAction(output, input, simulation));
            actions.add(new ResetSimulationMapAction(output, input, simulation));
            actions.add(new GetSimMapInfoAction(output, simulationMap));
            actions.add(new GetHelpAction(output, instructionText));
            actions.add(new ExitConsoleAction(output));

            new Main().runConsoleMenuLoop(input, output, actions);

        } else if (outputMode == 2) {
            SimMapRenderer simMapRenderer = new SwingSimMapRenderer(simulationMap,
                    simulation,
                    instructionText);
            simulation.setSimulationMapRenderer(simMapRenderer);

        } else {
            throw new IllegalArgumentException(
                    String.format("Invalid wayToDisplay parameter value: %s", outputMode));
        }
//        } catch (Exception e) {
//            System.out.println("Error occurred while running application");
//            System.out.printf("Error description: %s%n", e.getMessage());
//            if (e.getCause() != null && e.getCause().getMessage() != null) {
//                System.out.printf("Error cause: %s%n", e.getCause().getMessage());
//            }
//            System.out.println("Application terminated");
//        }
    }

    private static SimulationMap initSimMap() {
        int xMapSize = PropFileLoadHandler.getIntParam(PROP_FILE_NAME, "xMapSize");
        int yMapSize = PropFileLoadHandler.getIntParam(PROP_FILE_NAME, "yMapSize");
        if (xMapSize > 150) {
            throw new IllegalArgumentException("xMapSize parameter is set more than 150 points");
        }
        if (yMapSize > 75) {
            throw new IllegalArgumentException("yMapSize parameter is set more than 75 points");
        }
        return new TwoHashMapsSimulationMap(xMapSize,
                yMapSize,
                PropFileLoadHandler.getIntParam(PROP_FILE_NAME, "renewableResourcesCycleLength"));
    }

    private static List<Entity> initListOfEntities(SimulationMap simulationMap, PathSearchAlgo pathSearchAlgo) {
        int numOfPredators = PropFileLoadHandler.getIntParam(PROP_FILE_NAME, "numOfPredators");
        int numOfHerbivore = PropFileLoadHandler.getIntParam(PROP_FILE_NAME, "numOfHerbivore");
        int reproductionLimitOfGrassEatenForHerbivore =
                PropFileLoadHandler.getIntParam(PROP_FILE_NAME,
                        "reproductionResourceConsumptionLimitForHerbivore");
        int reproductionLimitOfCreatureEatenForPredator =
                PropFileLoadHandler.getIntParam(PROP_FILE_NAME,
                        "reproductionResourceConsumptionLimitForPredator");
        int percentageOfRocks = PropFileLoadHandler.getIntParam(PROP_FILE_NAME, "percentageOfRocks");
        int percentageOfGrasses = PropFileLoadHandler.getIntParam(PROP_FILE_NAME, "percentageOfGrasses");
        int percentageOfTrees = PropFileLoadHandler.getIntParam(PROP_FILE_NAME, "percentageOfTrees");

        List<Entity> listOfEntities = new ArrayList<>();
        int numOfPoints = simulationMap.getXMapSize() * simulationMap.getYMapSize();

        listOfEntities.addAll(EntityFactory.createNumOfEntities(EntityTypes.PREDATOR,
                simulationMap,
                pathSearchAlgo,
                reproductionLimitOfCreatureEatenForPredator,
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

    private static HashMap<Class<? extends Entity>, Character> initEntitiesMapper() {
        char maskForPredator = PropFileLoadHandler.getCharParam(PROP_FILE_NAME, "maskForPredator");
        char maskForHerbivore = PropFileLoadHandler.getCharParam(PROP_FILE_NAME, "maskForHerbivore");
        char maskForRock = PropFileLoadHandler.getCharParam(PROP_FILE_NAME, "maskForRock");
        char maskForTree = PropFileLoadHandler.getCharParam(PROP_FILE_NAME, "maskForTree");
        char maskForGrass = PropFileLoadHandler.getCharParam(PROP_FILE_NAME, "maskForGrass");

        HashMap<Class<? extends Entity>, Character> mapClassToChar = new HashMap<>();

        mapClassToChar.put(Predator.class, maskForPredator);
        mapClassToChar.put(Herbivore.class, maskForHerbivore);
        mapClassToChar.put(Rock.class, maskForRock);
        mapClassToChar.put(Tree.class, maskForTree);
        mapClassToChar.put(Grass.class, maskForGrass);
        return mapClassToChar;
    }

    private void runConsoleMenuLoop(Input input, Output output, List<UserAction> actions) {
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