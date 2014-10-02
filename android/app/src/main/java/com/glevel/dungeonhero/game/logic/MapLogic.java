package com.glevel.dungeonhero.game.logic;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.tmx.TMXTile;

import com.glevel.dungeonhero.game.GameUtils;
import com.glevel.dungeonhero.game.models.GameElement;
import com.glevel.dungeonhero.game.models.map.Map;
import com.glevel.dungeonhero.game.models.map.Tile;
import com.glevel.dungeonhero.game.models.map.Tile.TerrainType;
import com.glevel.dungeonhero.game.models.units.Soldier;
import com.glevel.dungeonhero.game.models.units.categories.Unit;
import com.glevel.dungeonhero.game.models.units.categories.Unit.Action;
import com.glevel.dungeonhero.game.models.units.categories.Vehicle;

public class MapLogic {

    private static final int RAYCASTER_STEP = 2;// in meters
    private static final int MINIMAL_DISTANCE_VISIBLE = 10;// in meters

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

    public static boolean canSee(Map map, GameElement g1, GameElement g2) {

        // hiding soldiers are more difficult to see
        if (g2 instanceof Soldier && !g2.isVisible()
                && getDistanceBetween(g1, g2) > 2 * MINIMAL_DISTANCE_VISIBLE * GameUtils.PIXEL_BY_METER) {
            Unit unit = (Unit) g2;
            if (unit.getCurrentAction() == Action.HIDING && unit.getTilePosition() != null
                    && unit.getTilePosition().getTerrain() != null && Math.random() < 0.9f) {
                return false;
            }
        }

        float destinationX = g2.getSprite().getX();
        float destinationY = g2.getSprite().getY();

        float dx = destinationX - g1.getSprite().getX();
        float dy = destinationY - g1.getSprite().getY();
        double angle = Math.atan(dy / dx);
        boolean hasArrived = false;

        float x = g1.getSprite().getX(), y = g1.getSprite().getY();

        List<TerrainType> lstTerrain = new ArrayList<TerrainType>();

        if (getDistanceBetween(g1, destinationX, destinationY) <= MINIMAL_DISTANCE_VISIBLE * GameUtils.PIXEL_BY_METER) {
            return true;
        }

        int stepInPixels = RAYCASTER_STEP * GameUtils.PIXEL_BY_METER;

        int n = 0;

        while (!hasArrived) {
            if (n > 30) {
                return false;
            }
            // go one step further
            if (dx > 0) {
                x += stepInPixels * Math.cos(angle);
                y += stepInPixels * Math.sin(angle);
            } else {
                x += -stepInPixels * Math.cos(angle);
                y += stepInPixels * Math.sin(angle + Math.PI);
            }

            dx = destinationX - x;
            dy = destinationY - y;

            TMXTile tmxTile = map.getTmxLayer().getTMXTileAt(x, y);
            if (tmxTile != null) {
                Tile t = map.getTiles()[tmxTile.getTileRow()][tmxTile.getTileColumn()];

                if (t.getContent() != null && t.getContent() == g2) {
                    return true;
                }

                if (g2 instanceof Soldier && t.getContent() != null && t.getContent() != g1
                        && t.getContent() instanceof Vehicle
                        && Math.sqrt(dx * dx + dy * dy) > GameUtils.PIXEL_BY_METER * 1) {
                    // target is a soldier and is hidden behind a vehicle
                    return false;
                }

                // lists the different different obstacles
                if ((t.getTerrain() == null || t.getTerrain().isBlockingVision())
                        && (lstTerrain.size() == 0 || t.getTerrain() != lstTerrain.get(lstTerrain.size() - 1))) {
                    lstTerrain.add(t.getTerrain());
                    if (lstTerrain.size() > 3) {
                        return false;
                    } else if ((getDistanceBetween(g1, x, y) > GameUtils.PIXEL_BY_METER * 3 || Math.sqrt(dx * dx + dy
                            * dy) > GameUtils.PIXEL_BY_METER * 3)
                            && lstTerrain.size() > 1) {
                        // target is behind an obstacle
                        return false;
                    }
                }
            }

            if (Math.sqrt(dx * dx + dy * dy) <= stepInPixels) {
                break;
            }

            n++;
        }

        return true;
    }

    public static boolean canSee(Map map, GameElement g1, float destinationX, float destinationY) {
        float dx = destinationX - g1.getSprite().getX();
        float dy = destinationY - g1.getSprite().getY();
        double angle = Math.atan(dy / dx);
        boolean hasArrived = false;

        float x = g1.getSprite().getX(), y = g1.getSprite().getY();

        List<TerrainType> lstTerrain = new ArrayList<TerrainType>();

        if (getDistanceBetween(g1, destinationX, destinationY) <= MINIMAL_DISTANCE_VISIBLE * GameUtils.PIXEL_BY_METER) {
            return true;
        }

        int stepInPixels = RAYCASTER_STEP * GameUtils.PIXEL_BY_METER;

        int n = 0;

        while (!hasArrived) {
            if (n > 30) {
                return false;
            }
            // go one step further
            if (dx > 0) {
                x += stepInPixels * Math.cos(angle);
                y += stepInPixels * Math.sin(angle);
            } else {
                x += -stepInPixels * Math.cos(angle);
                y += stepInPixels * Math.sin(angle + Math.PI);
            }

            dx = destinationX - x;
            dy = destinationY - y;

            TMXTile tmxTile = map.getTmxLayer().getTMXTileAt(x, y);
            if (tmxTile != null) {
                Tile t = map.getTiles()[tmxTile.getTileRow()][tmxTile.getTileColumn()];

                if (t.getContent() != null && t.getContent() != g1 && t.getContent() instanceof Vehicle
                        && Math.sqrt(dx * dx + dy * dy) > GameUtils.PIXEL_BY_METER * 1) {
                    // target is hidden behind a vehicle
                    return false;
                }

                // lists the different different obstacles
                if ((t.getTerrain() == null || t.getTerrain().isBlockingVision())
                        && (lstTerrain.size() == 0 || t.getTerrain() != lstTerrain.get(lstTerrain.size() - 1))) {
                    lstTerrain.add(t.getTerrain());
                    if (lstTerrain.size() > 3) {
                        return false;
                    } else if ((getDistanceBetween(g1, x, y) > GameUtils.PIXEL_BY_METER * 3 || Math.sqrt(dx * dx + dy
                            * dy) > GameUtils.PIXEL_BY_METER * 3)
                            && lstTerrain.size() > 1) {
                        // target is behind an obstacle
                        return false;
                    }
                }
            }

            if (Math.sqrt(dx * dx + dy * dy) <= stepInPixels) {
                break;
            }

            n++;
        }

        return true;
    }

    public static float[] getCoordinatesAfterTranslation(float xPosition, float yPosition, float distance,
            double angle, boolean isXPositive) {
        if (isXPositive) {
            return new float[] { (float) (xPosition + distance * Math.cos(angle)),
                    (float) (yPosition + distance * Math.sin(angle)) };
        } else {
            return new float[] { (float) (xPosition - distance * Math.cos(angle)),
                    (float) (yPosition + distance * Math.sin(angle + Math.PI)) };
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
