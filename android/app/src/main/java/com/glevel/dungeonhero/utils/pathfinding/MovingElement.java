package com.glevel.dungeonhero.utils.pathfinding;

public interface MovingElement<N extends Node> {

    boolean canMoveIn(N node);

}
