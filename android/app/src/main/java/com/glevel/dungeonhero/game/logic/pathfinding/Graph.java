package com.glevel.dungeonhero.game.logic.pathfinding;

import java.util.HashSet;
import java.util.Set;

public class Graph<N extends Node> {

    private N[][] nodes;

    public Graph(N[][] nodes) {
        this.nodes = nodes;
    }

    public Set<N> getAdjacentNodes(N node, boolean allowDiagonalMoves, MovingElement movingElement) {
        Set<N> adjacentNodes = new HashSet<N>();
        for (int y = node.getY() - 1; y < node.getY() + 2; y++) {
            for (int x = node.getX() - 1; x < node.getX() + 2; x++) {
                if (x >= 0 && x < nodes[0].length && y >= 0 && y < nodes.length
                        && (x != node.getX() || y != node.getY())) {
                    N n = nodes[y][x];
                    if (movingElement.canMoveIn(n) && (allowDiagonalMoves || calcManhattanDistance(node, n) == 1)) {
                        adjacentNodes.add(n);
                    }
                }
            }
        }
        return adjacentNodes;
    }

    public double calcManhattanDistance(N a, N b) {
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
    }

}
