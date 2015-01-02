package com.glevel.dungeonhero.utils.pathfinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

public class AStar<N extends Node> {

    public List<N> search(N[][] nodes, N source, N target, boolean allowDiagonalMoves, MovingElement movingElement) {
        // prepare sets
        Map<String, AStarNode<N>> openSet = new HashMap<String, AStarNode<N>>();
        PriorityQueue<AStarNode<N>> pQueue = new PriorityQueue<AStarNode<N>>(20, new AStarNodeComparator<N>());
        Map<String, AStarNode<N>> closeSet = new HashMap<String, AStarNode<N>>();

        // add source
        AStarNode<N> start = new AStarNode<N>(source, 0, MathUtils.calcManhattanDistance(source, target));
        openSet.put(source.getId(), start);
        pQueue.add(start);

        AStarNode<N> goal = null;
        while (openSet.size() > 0) {
            AStarNode<N> testedNode = pQueue.poll();
            openSet.remove(testedNode.getId());
            if (testedNode.getId().equals(target.getId())) {
                goal = testedNode;
                break;
            } else {
                closeSet.put(testedNode.getId(), testedNode);
                Set<N> neighbors = (Set<N>) MathUtils.getAdjacentNodes(nodes, testedNode.getNode(), 1, allowDiagonalMoves, movingElement);
                for (N neighbor : neighbors) {
                    AStarNode<N> visited = closeSet.get(neighbor.getId());
                    if (visited == null) {
                        double g = testedNode.getG() + MathUtils.calcManhattanDistance(testedNode.getNode(), neighbor);
                        AStarNode<N> n = openSet.get(neighbor.getId());

                        if (n == null) {
                            // not in the open set
                            n = new AStarNode<N>(neighbor, g, MathUtils.calcManhattanDistance(neighbor, target));
                            n.setCameFrom(testedNode);
                            openSet.put(neighbor.getId(), n);
                            pQueue.add(n);
                        } else if (g < n.getG()) {
                            // Have a better route to the current node, change its parent
                            n.setCameFrom(testedNode);
                            n.setG(g);
                            n.setH(MathUtils.calcManhattanDistance(neighbor, target));
                        }
                    }
                }
            }
        }

        // after the target is reached, playMusic to populate the path
        if (goal != null) {
            Stack<N> stack = new Stack<N>();
            stack.push(goal.getNode());
            List<N> list = new ArrayList<N>();
            AStarNode<N> parent = goal.getCameFrom();

            while (parent != null) {
                stack.push(parent.getNode());
                parent = parent.getCameFrom();
            }
            while (stack.size() > 0) {
                list.add(stack.pop());
            }

            return list;
        }

        return null;
    }

}
