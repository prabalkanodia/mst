package com.ads;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: prabal
 * Date: 10/19/13
 * Time: 3:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class GraphUtils {

    public static final int MAX_EDGE_COST = 1000;

    public enum STATUS {UNVISITED, VISITING, VISITED}

    public static int calculateE(int v, int d) {
        return (v * (v - 1) / 2) * (d / 100);
    }

    public static boolean validateVertices(int v, int d) {
        return v >= (100 * 2 / d); // getEdges >= (v - 1) => v = 200/d;
    }

    public static Graph createConnectedGraph(int v, int d) {
        Graph g;
        do {
            //g = GraphUtils.createGraph(v, d);
            g = GraphUtils.createQuickGraph(v, d);
        } while (!GraphUtils.isConnectedDFS(g));

        return g;
    }

    public static Graph createGraph(int v, int d) {
        int e = calculateE(v, d);
        Graph g = new Graph(v);
        HashSet<Edge> edges = new HashSet<Edge>();

        while (g.getE() <= e) {
            Random r = new Random();
            int x = r.nextInt(v);
            int y = r.nextInt(v);
            int cost = r.nextInt(MAX_EDGE_COST) + 1;

            Edge edge = new Edge(x, y, cost);
            if (x != y && !edges.contains(edge)) {
                edges.add(edge);
                g.addEdge(x, y);
            }
        }

        return g;
    }

    public static Graph createQuickGraph(int v, int d) {
        int e = calculateE(v, d);
        Graph g = new Graph(v);
        HashSet<Edge> edges = new HashSet<Edge>();

        ArrayList<Integer> list1 = new ArrayList<Integer>();
        ArrayList<Integer> list2 = new ArrayList<Integer>();

        for (int i = 0; i < v; i++)
            list1.add(i);

        // Bootstrap
        Collections.shuffle(list1);
        int x = list1.remove(0);
        Collections.shuffle(list1);
        int y = list1.remove(0);

        // Add this edge to hashset
        Random rnd = new Random();
        int c = rnd.nextInt(MAX_EDGE_COST) + 1;
        Edge edge = new Edge(x, y, c);
        edges.add(edge);
        g.addEdge(x, y);

        // Add to processed list
        list2.add(x);
        list2.add(y);

        // Ensure a tree is created
        while (!list1.isEmpty()) {
            Collections.shuffle(list1);
            int from = list1.remove(0);
            Collections.shuffle(list2);
            int to = list2.get(0);
            Random r = new Random();
            int cost = r.nextInt(MAX_EDGE_COST) + 1;

            Edge link = new Edge(from, to, cost);
            if (from != to && !edges.contains(link)) {
                list2.add(from);
                edges.add(link);
                g.addEdge(from, to);
            }
        }

        // All elements are in list2. Decorate the tree to form a graph
        while (g.getE() < e) {
            Collections.shuffle(list2);
            int from = list2.get(0);
            Collections.shuffle(list2);
            int to = list2.get(0);
            Random r = new Random();
            int cost = r.nextInt(MAX_EDGE_COST) + 1;

            Edge link = new Edge(from, to, cost);
            if (from != to && !edges.contains(link)) {
                edges.add(link);
                g.addEdge(from, to);
            }
        }

        return g;
    }


    public static boolean isConnectedDFS(Graph g) {
        int v = g.getV();
        Enum<STATUS>[] status = (Enum<STATUS>[]) new Enum[v];

        // Vertices are labeled 0 to n-1
        for (int i = 0; i < g.getV(); i++)
            status[i] = STATUS.UNVISITED;

        for (int i = 0; i < g.getV(); i++)
            visitV(g, i, status);

        for (int i = 0; i < g.getV(); i++)
            if (status[i] != STATUS.VISITED)
                return false;

        return true;
    }

    private static void visitV(Graph g, int u, Enum<STATUS>[] status) {
        status[u] = STATUS.VISITING;

        for (int v : g.adjList(u)) {
            if (status[v] == STATUS.UNVISITED)
                visitV(g, v, status);
        }

        status[u] = STATUS.VISITED;
    }
}
