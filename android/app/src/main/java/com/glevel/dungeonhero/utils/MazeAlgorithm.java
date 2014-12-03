package com.glevel.dungeonhero.utils;

import com.glevel.dungeonhero.utils.pathfinding.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by guillaume on 12/2/14.
 */
public class MazeAlgorithm {

    public static boolean[][] createMaze(int width, int height) {
        boolean[][] doors = new boolean[2 * height - 1][width];
        MazeNode[][] maze = new MazeNode[height][width];
        HashMap<Integer, List<MazeNode>> mapIndexNode = new HashMap<>(height * width);
        int nbWallsOpen = 0;

        // initialize the maze and the indexes
        int index = 0;
        List<MazeNode> l;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                maze[i][j] = new MazeNode(j, i, index);
                l = new ArrayList<>();
                l.add(maze[i][j]);
                mapIndexNode.put(index, l);
                index++;
            }
        }

        int xWall, yWall;
        boolean wallToOpen;
        boolean areSameIndexes;
        int newIndex, indexToUpdate;
        while (nbWallsOpen < width * height - 1) {
            do {
                // get a random wall
                yWall = ((int) (Math.random() * doors.length));
                xWall = ((int) (Math.random() * (doors[0].length + (yWall % 2 == 0 ? -1 : 0))));
                wallToOpen = doors[yWall][xWall];
                System.out.println(xWall + "," + yWall);

                // check node indexes are different
                indexToUpdate = maze[yWall / 2][xWall].getIndex();
                newIndex = yWall % 2 == 0 ? maze[yWall / 2][xWall + 1].getIndex() : maze[yWall / 2 + 1][xWall].getIndex();
                areSameIndexes = newIndex == indexToUpdate;
            } while (wallToOpen || areSameIndexes);

            // open door
            doors[yWall][xWall] = true;
            nbWallsOpen++;

            // update nodes indexes
            List<MazeNode> listToUpdate = mapIndexNode.get(indexToUpdate);
            for (MazeNode node : listToUpdate) {
                node.setIndex(newIndex);
                mapIndexNode.get(newIndex).add(node);
            }
            mapIndexNode.remove(indexToUpdate);
        }

        return doors;
    }

    public static void main(String[] args) {
        createMaze(3, 3);
    }

    private static class MazeNode implements Node {

        private final int x;
        private final int y;
        private int index;

        private MazeNode(int x, int y, int index) {
            this.x = x;
            this.y = y;
            this.index = index;
        }

        @Override
        public String getId() {
            return null;
        }

        @Override
        public int getX() {
            return x;
        }

        @Override
        public int getY() {
            return y;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

    }

}
