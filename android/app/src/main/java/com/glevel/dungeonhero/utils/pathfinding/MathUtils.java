package com.glevel.dungeonhero.utils.pathfinding;

import com.glevel.dungeonhero.game.base.GameElement;

import org.andengine.entity.sprite.Sprite;

import java.util.HashSet;
import java.util.Set;

public class MathUtils {

    public static double calcManhattanDistance(Node a, Node b) {
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
    }

    public static float getDistanceBetween(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)));
    }

    public static float getDistanceBetween(GameElement g1, GameElement g2) {
        return getDistanceBetween(g1.getSprite().getX(), g1.getSprite().getY(), g2.getSprite().getX(), g2.getSprite().getY());
    }

    public static float getDistanceBetween(GameElement g1, float x, float y) {
        return getDistanceBetween(g1.getSprite().getX(), g1.getSprite().getY(), x, y);
    }

    public static float[] getCoordinatesAfterTranslation(float xPosition, float yPosition, float distance, double angle, boolean isXPositive) {
        if (isXPositive) {
            return new float[]{(float) (xPosition + distance * Math.cos(angle)), (float) (yPosition + distance * Math.sin(angle))};
        } else {
            return new float[]{(float) (xPosition - distance * Math.cos(angle)), (float) (yPosition + distance * Math.sin(angle + Math.PI))};
        }
    }

    public static float getAngle(Sprite sprite, float xDestination, float yDestination) {
        float dx = xDestination - sprite.getX();
        float dy = yDestination - sprite.getY();
        float finalAngle = (float) (Math.atan(dy / dx) * 180 / Math.PI);
        if (dx > 0) {
            finalAngle += 90;
        } else {
            finalAngle -= 90;
        }
        return finalAngle - sprite.getRotation();
    }

    public static <E extends Node> Set<E> getAdjacentNodes(E[][] nodes, E centerNode, int step, boolean withDiagonal, MovingElement movingElement) {
        Set<E> adjacentNodes = new HashSet<E>();

        for (int y = centerNode.getY() - step; y < centerNode.getY() + step + 1; y++) {
            for (int x = centerNode.getX() - step; x < centerNode.getX() + step + 1; x++) {
                if (x >= 0 && x < nodes[0].length && y >= 0 && y < nodes.length && (x != centerNode.getX() || y != centerNode.getY())) {
                    E node = nodes[y][x];
                    if (node != null && (withDiagonal || calcManhattanDistance(centerNode, node) <= step) && (movingElement == null || movingElement.canMoveIn(node))) {
                        adjacentNodes.add(node);
                    }
                }
            }
        }

        return adjacentNodes;
    }

}
