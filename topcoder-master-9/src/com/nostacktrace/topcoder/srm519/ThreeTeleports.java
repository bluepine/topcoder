package com.nostacktrace.topcoder.srm519;


import java.awt.*;

/**
 * SRM 519 ThreeTeleports
 *
 * http://community.topcoder.com/stat?c=problem_statement&pm=11554&rd=14544
 *
 * This problem is a shortest path problem.  A frog needs to move from
 * a starting point to a goal point in the least number of hops.  He
 * can move from 1 space in 1 step.  There are no obstacles.  However,
 * there are 3 sets of telport pairs.  Any teleport point can teleport
 * the frog to the pair point at a cost of 10 steps.
 *
 * Since there are no obstacles and the number of of teleports is low, the
 * simplest solution is to create a graph with vertices for the start and
 * goal points.  Each teleport pair is also a point.  I construct an edge
 * between each point, with the cost being either the grid distance or 10
 * if the points are connected by a teleport.
 *
 * I used an adjacency matrix to represent the state.  Since the number of nodes
 * is very low, I've used FloydWarshall to compute the full all pairs shortest path
 * algorithm. I've used the java.awt.Point class to represent the 2d point.
 *
 */
public class ThreeTeleports {

    private Point[][] parseTeleports(String[] teleports) {
        Point[][] points = new Point[teleports.length][2];

        for (int i=0; i<teleports.length; i++) {
            String[] parts = teleports[i].split(" ");
            Point from = new Point(Integer.valueOf(parts[0]), Integer.valueOf(parts[1]));
            Point to   = new Point(Integer.valueOf(parts[2]), Integer.valueOf(parts[3]));

            points[i][0] = from;
            points[i][1] = to;
        }

        return points;
    }

    // compute the grid distance between two points.
    private int distance(Point point1, Point point2) {
        return Math.abs(point1.x-point2.x) + Math.abs(point1.y-point2.y);
    }

    // map an i,j in the teleport list to node number
    private int teleportIndex(int teleportNumber, int fromto) {
        return 2 + teleportNumber*2 + fromto;
    }

    // Floyd-Warshall
    private void computeShortestPaths(long[][] path) {
        for (int k=0; k<path.length; k++) {
            for (int i=0; i<path.length; i++) {
                for (int j = 0; j < path.length; j++) {
                    long dist = path[i][k] + path[k][j];

                    if (dist < path[i][j]) {
                        path[i][j] = dist;
                    }

                }
            }
        }
    }


    public int shortestDistance(int xMe, int yMe, int xHome, int yHome, String[] teleports) {
        Point start = new Point(xMe, yMe);
        Point goal  = new Point(xHome, yHome);

        Point[][] teleportList = parseTeleports(teleports);

        long[][] path = new long[8][8];


        path[0][1] = distance(start,goal);
        for (int i=0; i<teleportList.length; i++) {
            // link the telport points at cost 10
            path[teleportIndex(i,0)][teleportIndex(i,1)] = 10;
            path[teleportIndex(i,1)][teleportIndex(i,0)] = 10;

            // link start point to both teleport point ends at real cost
            path[0][teleportIndex(i,0)] = distance(start, teleportList[i][0]);
            path[0][teleportIndex(i,1)] = distance(start, teleportList[i][1]);

            // link each telport point to goal at real cost
            path[teleportIndex(i,0)][1] = distance(teleportList[i][0], goal);
            path[teleportIndex(i,1)][1] = distance(teleportList[i][1], goal);

            // add link all pairs of telport points at walking cost
            // there's a little duplicate logic here, but it's minimal
            // don't worry if the points have a walking distance closer than the teleport costs
            // since the nodes are fully connected, because for any path A->B->C there will already be
            // a path from A->C that represents the grid distance
            for (int j=0; j<teleportList.length; j++) {
                if (i!=j) {
                    path[teleportIndex(i,0)][teleportIndex(j,0)] = distance(teleportList[i][0],teleportList[j][0]);
                    path[teleportIndex(i,0)][teleportIndex(j,1)] = distance(teleportList[i][0],teleportList[j][1]);
                    path[teleportIndex(i,1)][teleportIndex(j,0)] = distance(teleportList[i][1],teleportList[j][0]);
                    path[teleportIndex(i,1)][teleportIndex(j,1)] = distance(teleportList[i][1],teleportList[j][1]);

                    path[teleportIndex(j,0)][teleportIndex(i,0)] = distance(teleportList[j][0],teleportList[i][0]);
                    path[teleportIndex(j,0)][teleportIndex(i,1)] = distance(teleportList[j][0],teleportList[i][1]);
                    path[teleportIndex(j,1)][teleportIndex(i,0)] = distance(teleportList[j][1],teleportList[i][0]);
                    path[teleportIndex(j,1)][teleportIndex(i,1)] = distance(teleportList[j][1],teleportList[i][1]);
                }
            }
        }

        computeShortestPaths(path);

        // shortest path will always be an int since the initial path from start to goal fit
        // in and the final path will be no greater
        return (int) path[0][1];
    }



}
