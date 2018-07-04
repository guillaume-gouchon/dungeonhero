package com.glevel.dungeonhero.utils.pathfinding;

import java.util.Comparator;

class AStarNodeComparator<N extends Node> implements Comparator<AStarNode<N>> {

    public int compare(AStarNode<N> first, AStarNode<N> second) {
        return Double.compare(first.getF(), second.getF());
    }

}
