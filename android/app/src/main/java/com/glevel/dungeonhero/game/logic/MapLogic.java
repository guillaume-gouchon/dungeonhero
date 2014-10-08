package com.glevel.dungeonhero.game.logic;

import com.glevel.dungeonhero.game.models.GameElement;
import com.glevel.dungeonhero.game.models.map.Map;
import com.glevel.dungeonhero.game.models.map.Tile;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.tmx.TMXTile;

import java.util.ArrayList;
import java.util.List;

public class MapLogic {

    public static float getDistanceBetween(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)));
    }

    public static float getDistanceBetween(GameElement g1, GameElement g2) {
        return getDistanceBetween(g1.getSprite().getX(), g1.getSprite().getY(), g2.getSprite().getX(), g2.getSprite()
                .getY());
    }

    public static int getDistance(Tile tile1, Tile tile2) {
        return Math.abs(tile1.getX() - tile2.getX()) + Math.abs(tile1.getY() - tile2.getY());
    }

    public static float getDistanceBetween(GameElement g1, float x, float y) {
        return getDistanceBetween(g1.getSprite().getX(), g1.getSprite().getY(), x, y);
    }

    public static float[] getCoordinatesAfterTranslation(float xPosition, float yPosition, float distance,
                                                         double angle, boolean isXPositive) {
        if (isXPositive) {
            return new float[]{(float) (xPosition + distance * Math.cos(angle)),
                    (float) (yPosition + distance * Math.sin(angle))};
        } else {
            return new float[]{(float) (xPosition - distance * Math.cos(angle)),
                    (float) (yPosition + distance * Math.sin(angle + Math.PI))};
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

    public static List<Tile> getAdjacentTiles(Map map, Tile centerTile, int step, boolean withDiagonal) {
        List<Tile> adjacentTiles = new ArrayList<Tile>();

        for (int y = centerTile.getY() - step; y < centerTile.getY() + step + 1; y++) {
            for (int x = centerTile.getX() - step; x < centerTile.getX() + step + 1; x++) {
                if (x >= 0 && x < map.getWidth() && y >= 0 && y < map.getHeight()
                        && (x != centerTile.getX() || y != centerTile.getY())) {
                    Tile t = map.getTiles()[y][x];

                    if (withDiagonal || getDistance(centerTile, t) <= step) {
                        adjacentTiles.add(t);
                    }
                }
            }
        }

        return adjacentTiles;
    }

    public static Tile getTileAtCoordinates(Map map, float x, float y) {
        TMXTile tmxTile = map.getTmxLayer().getTMXTileAt(x, y);
        if (tmxTile != null) {
            return map.getTiles()[tmxTile.getTileRow()][tmxTile.getTileColumn()];
        } else {
            return null;
        }
    }

}
