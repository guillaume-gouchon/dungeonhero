package com.glevel.dungeonhero.utils.pathfinding;

public interface MovingElement<N extends Node> {

    public abstract boolean canMoveIn(N node);

}
