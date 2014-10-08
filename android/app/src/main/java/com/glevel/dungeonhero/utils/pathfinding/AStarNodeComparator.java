package com.glevel.dungeonhero.utils.pathfinding;

import java.util.Comparator;

public class AStarNodeComparator<N extends Node> implements Comparator<AStarNode<N>> {

    public int compare(AStarNode<N> first, AStarNode<N> second) {
        if (first.getF() < second.getF()) {
            return -1;
        } else if (first.getF() > second.getF()) {
            return 1;
        } else {
            return 0;
        }
    }

}
