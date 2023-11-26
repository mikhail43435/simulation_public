package ru.hmp.simulation.pathsearch;

import ru.hmp.simulation.map.Position;

import java.util.List;
import java.util.Optional;

public interface PathSearchAlgo {
    Optional<List<Position>> findPath(int[][] grid, Position startPosition, Position endPosition);
}
