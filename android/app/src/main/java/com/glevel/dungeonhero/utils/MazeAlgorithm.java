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
        MazeNode[][] maze = new MazeNode[height][width];
        HashMap<Integer, List<MazeNode>> mapIndexNode = new HashMap<>(height * width);
        int nbWallsOpen = 0;

        boolean[][] verticalWalls = new boolean[height][width - 1];
        boolean[][] horizontalWalls = new boolean[height - 1][width];

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

        boolean[][] chosenWalls;
        boolean wallToOpen;
        int xWall, yWall;
        boolean areDifferentIndexes;
        int newIndex, indexToUpdate;
        while (nbWallsOpen < width * height - 1) {
            System.out.println("" + nbWallsOpen);
            // open a random wall
            do {
                chosenWalls = Math.random() < 0.5 ? verticalWalls : horizontalWalls;
                yWall = ((int) (Math.random() * chosenWalls.length));
                xWall = ((int) (Math.random() * chosenWalls[0].length));
                wallToOpen = chosenWalls[yWall][xWall];

                newIndex = chosenWalls == verticalWalls ? maze[yWall][xWall + 1].getIndex() : maze[yWall + 1][xWall].getIndex();
                indexToUpdate = maze[yWall][xWall].getIndex();
                areDifferentIndexes = newIndex != indexToUpdate;
            } while (wallToOpen || !areDifferentIndexes);

            System.out.println("open wall " + xWall + "," + yWall);
            chosenWalls[yWall][xWall] = true;
            nbWallsOpen++;

            // update nodes indexes
            List<MazeNode> listToUpdate = mapIndexNode.get(indexToUpdate);
            for (MazeNode node : listToUpdate) {
                node.setIndex(newIndex);
                mapIndexNode.get(newIndex).add(node);
            }
            mapIndexNode.remove(indexToUpdate);
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width - 1; j++) {
                if (verticalWalls[i][j]) {
                    System.out.print("x0");
                } else {
                    System.out.print("x8");
                }
            }
            System.out.print("x");
            System.out.println("");

            if (i < height - 1) {
                for (int j = 0; j < width; j++) {
                    if (horizontalWalls[i][j]) {
                        System.out.print("0 ");
                    } else {
                        System.out.print("8 ");
                    }
                }
                System.out.println("");
            }
        }


        return null;
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
