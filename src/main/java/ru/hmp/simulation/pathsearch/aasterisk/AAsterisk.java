package ru.hmp.simulation.pathsearch.aasterisk;

import ru.hmp.simulation.map.Position;
import ru.hmp.simulation.pathsearch.PathSearchAlgo;

import java.util.*;

/**
 * Source code was taken from https://codegym.cc/groups/posts/a-search-algorithm-in-java
 */
public class AAsterisk implements PathSearchAlgo {

    @Override
    public Optional<List<Position>> findPath(int[][] grid, Position startPosition, Position endPosition) {
        return aStarSearch(grid, startPosition, endPosition);
    }

    private boolean isValid(int rows,
                            int cols,
                            Pair point) {
        if (rows > 0 && cols > 0) {
            return (point.xPoint >= 0)
                    && (point.xPoint < rows)
                    && (point.yPoint >= 0)
                    && (point.yPoint < cols);
        }
        return false;
    }

    private boolean isUnBlocked(int[][] grid,
                                int rows,
                                int cols,
                                Pair point) {
        return isValid(rows, cols, point) && grid[point.xPoint][point.yPoint] == 0;
    }

    private boolean isDestination(Pair position, Pair dest) {
        return position == dest || position.equals(dest);
    }

    private double calculateHValue(Pair src, Pair dest) {
        return Math.sqrt(Math.pow((src.xPoint - dest.xPoint), 2.0) + Math.pow((src.yPoint - dest.yPoint), 2.0));
    }

    private List<Position> tracePath(
            Cell[][] cellDetails,
            Pair dest) {

        Deque<Pair> path = new ArrayDeque<>();

        int row = dest.xPoint;
        int col = dest.yPoint;

        Pair nextNode;
        do {
            path.push(new Pair(row, col));
            nextNode = cellDetails[row][col].parent;
            row = nextNode.xPoint;
            col = nextNode.yPoint;
        } while (cellDetails[row][col].parent != nextNode); // until src

        List<Position> pathList = new ArrayList<>();

        while (!path.isEmpty()) {
            Pair p = path.peek();
            path.pop();
            pathList.add(Position.of(p.yPoint, p.xPoint));
        }
        return pathList;
    }

    private Optional<List<Position>> aStarSearch(int[][] grid,
                                                 Position startPosition,
                                                 Position endPosition
    ) {
        int rows = grid.length;
        int cols = grid[0].length;
        Pair src = new Pair(startPosition.getY(), startPosition.getX());
        Pair dest = new Pair(endPosition.getY(), endPosition.getX());

        if (!isValid(rows, cols, src)
                || !isValid(rows, cols, dest)
                || isDestination(src, dest)) {
            return Optional.empty();
        }

        boolean[][] closedList = new boolean[rows][cols];

        Cell[][] cellDetails = new Cell[rows][cols];

        int xCell;
        int yCell;
        // Initialising of the starting cell
        xCell = src.xPoint;
        yCell = src.yPoint;
        cellDetails[xCell][yCell] = new Cell();
        cellDetails[xCell][yCell].f = 0.0;
        cellDetails[xCell][yCell].g = 0.0;
        cellDetails[xCell][yCell].h = 0.0;
        cellDetails[xCell][yCell].parent = new Pair(xCell, yCell);

        // Creating an open list
        PriorityQueue<Details> openList = new PriorityQueue<>((o1, o2) -> (int) Math.round(o1.value - o2.value));

        // Put the starting cell on the open list,   set f.startCell = 0

        openList.add(new Details(0.0, xCell, yCell));

        while (!openList.isEmpty()) {
            Details p = openList.peek();
            // Add to the closed list
            xCell = p.i; // second element of tuple
            yCell = p.j; // third element of tuple

            // Remove from the open list
            openList.poll();
            closedList[xCell][yCell] = true;

            // Generating all the 8 neighbors of the cell
            for (int addX = -1; addX <= 1; addX++) {
                for (int addY = -1; addY <= 1; addY++) {
                    Pair neighbour = new Pair(xCell + addX, yCell + addY);
                    if (isValid(rows, cols, neighbour)) {
                        if (cellDetails[neighbour.xPoint] == null) {
                            cellDetails[neighbour.xPoint] = new Cell[cols];
                        }
                        if (cellDetails[neighbour.xPoint][neighbour.yPoint] == null) {
                            cellDetails[neighbour.xPoint][neighbour.yPoint] = new Cell();
                        }

                        if (isDestination(neighbour, dest)) {
                            cellDetails[neighbour.xPoint][neighbour.yPoint].parent = new Pair(xCell, yCell);
                            return Optional.of(tracePath(cellDetails, dest));
                        } else if (!closedList[neighbour.xPoint][neighbour.yPoint]
                                && isUnBlocked(grid, rows, cols, neighbour)) {
                            double gNew, hNew, fNew;
                            gNew = cellDetails[xCell][yCell].g + 1.0;
                            hNew = calculateHValue(neighbour, dest);
                            fNew = gNew + hNew;

                            if (cellDetails[neighbour.xPoint][neighbour.yPoint].f == -1
                                    || cellDetails[neighbour.xPoint][neighbour.yPoint].f > fNew) {

                                openList.add(new Details(fNew, neighbour.xPoint, neighbour.yPoint));

                                // Update the details of this
                                // cell
                                cellDetails[neighbour.xPoint][neighbour.yPoint].g = gNew;
                                //heuristic function cellDetails[neighbour.first][neighbour.second].h = hNew;
                                cellDetails[neighbour.xPoint][neighbour.yPoint].f = fNew;
                                cellDetails[neighbour.xPoint][neighbour.yPoint].parent = new Pair(xCell, yCell);
                            }
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }

    private static class Pair {
        int xPoint;
        int yPoint;

        public Pair(int xPoint, int yPoint) {
            this.xPoint = xPoint;
            this.yPoint = yPoint;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Pair)) return false;
            Pair pair = (Pair) o;
            return xPoint == pair.xPoint && yPoint == pair.yPoint;
        }

        @Override
        public int hashCode() {
            return Objects.hash(xPoint, yPoint);
        }
    }

    // Creating a shortcut for tuple<int, int, int> type
    private static class Details {
        double value;
        int i;
        int j;

        public Details(double value, int i, int j) {
            this.value = value;
            this.i = i;
            this.j = j;
        }
    }

    // a Cell (node) structure
    private static class Cell {
        public Pair parent;
        // f = g + h, where h is heuristic
        public double f;
        public double g;
        public double h;

        Cell() {
            parent = new Pair(-1, -1);
            f = -1;
            g = -1;
            h = -1;
        }

        public Cell(Pair parent, double f, double g, double h) {
            this.parent = parent;
            this.f = f;
            this.g = g;
            this.h = h;
        }
    }

}