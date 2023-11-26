package ru.hmp.simulation.model;

import ru.hmp.simulation.EntityFactory;
import ru.hmp.simulation.map.Position;
import ru.hmp.simulation.map.SimulationMap;
import ru.hmp.simulation.pathsearch.PathSearchAlgo;

public class Herbivore extends Creature {

    private int grassEaten;

    public Herbivore(SimulationMap simulationMap,
                     PathSearchAlgo pathSearchAlgo,
                     int reproductionLimit) {
        super(simulationMap, pathSearchAlgo, reproductionLimit);
    }

    /**
     * 1 если ЦЕЛЬ не установлена ИЛИ цели нет на карте ТО
     * обнуляем цель
     * ищем новую цель
     * ЕСЛИ цели нет - выход
     * 2 если ПУТЬ не установлен ИЛИ если путь не получилось установить
     * выход
     * 3 если следующий ШАГ занят то
     * если это ЦЕЛЬ - удаляем цель с карты, зануляем цель, зануляем путь
     * если это НЕ ЦЕЛЬ - очищаем путь и выход
     * 4 делаем перемещение на следующий шаг
     */
    @Override
    public int makeMove() {
        if (grassEaten == reproductionLimit) {
            createNewHerbivore();
            grassEaten = 0;
            return 0;
        }

        if (targetEntity == null || !simulationMap.isEntityOnMap(targetEntity)) {
            targetEntity = null;
            setTargetOfGiveType(EntityTypes.GRASS);
            if (this.targetEntity == null) {
                return 0;
            }
        }

        if (this.pathToTarget == null || this.pathToTarget.isEmpty()) {
            pathToTarget = null;
            setPathToTarget(targetEntity);
            if (this.pathToTarget == null) {
                return 0;
            }
        }

        Position nextPosition = this.pathToTarget.removeFirst();

        if (!simulationMap.isPositionEmpty(nextPosition)) {
            Entity entityAtNextPosition = simulationMap.getEntityAtPosition(nextPosition).orElseThrow();
            if (entityAtNextPosition.equals(this.targetEntity)) {
                simulationMap.removeEntity(targetEntity);
                targetEntity = null;
                pathToTarget = null;
                grassEaten++;
            } else if (entityAtNextPosition.getClass() == EntityTypes.GRASS.getClassType()) {
                simulationMap.removeEntity(entityAtNextPosition);
                grassEaten++;
            } else {
                this.pathToTarget = null;
                return 0;
            }
        }
        simulationMap.updateEntityPosition(this, nextPosition);
        return 1;
    }

    private void createNewHerbivore() {
        simulationMap.addEntityRightNextPosition(EntityFactory.createEntity(EntityTypes.HERBIVORE,
                simulationMap,
                pathSearchAlgo,
                reproductionLimit),
                simulationMap.getEntityPosition(this));
    }
}