package ru.hmp.simulation.model.entities;

import ru.hmp.simulation.map.Position;
import ru.hmp.simulation.map.SimulationMap;
import ru.hmp.simulation.model.Creature;
import ru.hmp.simulation.model.Entity;
import ru.hmp.simulation.model.EntityTypes;
import ru.hmp.simulation.model.Mobile;
import ru.hmp.simulation.pathsearch.PathSearchAlgo;

public final class Herbivore extends Creature implements Mobile {

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
        if (resourceConsumptionCounter == resourceConsumptionLimit) {
            createNewEntityNearBy(EntityTypes.HERBIVORE);
            resourceConsumptionCounter = 0;
            return 0;
        }
        if (targetEntity == null
                || !simulationMap.isEntityOnMap(targetEntity)
        ) {
            targetEntity = null;
            pathToTarget = null;
            if (!setClosestTarget(EntityTypes.GRASS)) {
                return 0;
            }
        }

        if (pathToTarget != null && simulationMap.getEntityPosition(targetEntity) != pathToTarget.peekLast()) {
            pathToTarget = null;
        }

        if (pathToTarget == null && !setPathToTarget(targetEntity)) {
            pathToTarget = null;
            targetEntity = null;
            return 0;
        }

        Position nextPositionToMove = this.pathToTarget.removeFirst();

        if (!simulationMap.isPositionEmpty(nextPositionToMove)) {

            Entity entityAtNextPosition = simulationMap.getEntityAtPosition(nextPositionToMove).orElseThrow();

            if (entityAtNextPosition.getClass() == EntityTypes.GRASS.getClassType()) {
                simulationMap.removeEntity(entityAtNextPosition);
                targetEntity = null;
                pathToTarget = null;
                resourceConsumptionCounter++;
            } else {
                this.pathToTarget = null;
                return 0;
            }
        }
        simulationMap.updateEntityPosition(this, nextPositionToMove);
        return 1;
    }
}