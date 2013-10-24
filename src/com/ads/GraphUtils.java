package com.ads;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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

    public enum Status {UNVISITED, VISITING, VISITED}

    public static long calculateE(int v, int d) {
        return ((v - 1)) * ((d * v) / (2 * 100));
    }

    public static boolean validateVertices(int v, int d) {
        return v >= (100 * 2 / d); // getEdges >= (v - 1) => v = 200/d;
    }

    public static Graph createConnectedGraph(int v, int d) {
        Graph g;
        long start = System.currentTimeMillis();
        do {
            //g = GraphUtils.createGraph(v, d);
            g = GraphUtils.createQuickGraph(v, d);
        } while (!GraphUtils.isConnectedDFS(g));

        long end = System.currentTimeMillis();
        System.out.println("Graph generation time: " + (end - start)/1000);

        return g;
    }

    public static Graph createGraph(String filename) {
        Graph g = null;
        try {
            BufferedReader input = new BufferedReader(new FileReader(filename));
            String line = null;
            int numV = 0;
            int numE = 0;

            // Read num of vertices and num of edges
            if ((line = input.readLine()) != null) {
                String[] words = line.split("\\s+");
                if (words.length != 2)
                    throw new Exception("Format of first line in the file required: numV numE");

                numV = Integer.parseInt(words[0]);
                numE = Integer.parseInt(words[1]);
            }

            if (numV <  0 || numE < 0 ) throw new Exception("numV, numE must be positive integers");
            if (numE < (numV - 1)) throw new Exception("Too few edges - Min required is (numV - 1)");
            if (numE > (numV * (numV - 1) / 2)) throw new Exception("Too many edges - Max edges can be numV choose 2");

            // Initialize graph
            g = new Graph(numV);

            // count of edges
            long count = 0; // must match numE at end
            // Read edges (from, to, cost)
            while ((line = input.readLine().trim()) != null && !("".equals(line))) {
                String[] edgeInfo = line.split("\\s+");
                if (edgeInfo.length != 3)
                    throw new Exception("Edge format required - from to cost");

                //g.addEdge(Integer.parseInt(edgeInfo[0]), Integer.parseInt(edgeInfo[1]));
                g.addEdge(
                        new Edge(
                            Integer.parseInt(edgeInfo[0]), Integer.parseInt(edgeInfo[1]), Integer.parseInt(edgeInfo[2])
                        )
                );
                count++;
            }

            if (count != numE) throw new Exception("numE and count of edges in the file are not equal");
            if (!GraphUtils.isConnectedDFS(g))
                throw new Exception("Vertices and Edges do not form a connected graph");

        } catch (Exception e) {
            System.out.println("File Exception: " + e);
        }

        return g;
    }

    public static Graph createGraph(int v, int d) {
        long e = calculateE(v, d);
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
                //g.addEdge(x, y);
                g.addEdge(edge);
            }
        }

        return g;
    }

    public static Graph createQuickGraph(int v, int d) {
        long e = calculateE(v, d);
        Graph g = new Graph(v);

        Vertex[] vertices = new Vertex[v];
        for (int i = 0; i < v; i++) {
            vertices[i] = new Vertex(i);
        }

        boolean[][] marked = new boolean[v][v];
        for (int i = 0; i < v; i++)
            for (int j = 0; j < v; j++) {
                marked[i][j] = false;
            }

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
        Edge edge = new Edge(vertices[x], vertices[y], c);
        //edges.add(edge);
        //g.addEdge(x, y);
        g.addEdge(edge);
        marked[x][y] = true;
        marked[y][x] = true;

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

            if (from != to && !marked[from][to]) {
                list2.add(from);
                //edges.add(link);
                //g.addEdge(from, to);
                Edge link = new Edge(vertices[from], vertices[to], cost);
                g.addEdge(link);
                marked[from][to] = true;
                marked[to][from] = true;
            }
        }

        // All elements are in list2. Decorate the tree to form a graph
        long count = g.getE();
        while (count < e) {
            //Collections.shuffle(list2);
            Random r = new Random();
            //int from = list2.get(0);
            int from = r.nextInt(v);
            //int to = list2.get(1);
            int to = r.nextInt(v);
            //if (from != to && !g.edges().contains(link)) {
            if (from != to && !marked[from][to]) {
                //Random r = new Random();
                //int cost = r.nextInt(MAX_EDGE_COST) + 1;

                Edge link = new Edge(vertices[from], vertices[to], r.nextInt(MAX_EDGE_COST) + 1);

                //edges.add(link);
                marked[from][to] = true;
                marked[to][from] = true;
                g.addEdge(link);
                count++;
                //g.addEdge(from, to);
            }
        }

        return g;
    }

//    public static Graph createQuickGraph(int v, int d) {
//        long e = calculateE(v, d);
//        Graph g = new Graph(v);
//
//        boolean[][] adjMatrix = new boolean[v][v];
//        for (int i = 0; i < v; i++)
//            for (int j = 0; j < v; j++) {
//                adjMatrix[i][j] = false;
//            }
//        //HashSet<Edge> edges = new HashSet<Edge>();
//
//        ArrayList<Integer> list1 = new ArrayList<Integer>();
//        ArrayList<Integer> list2 = new ArrayList<Integer>();
//
//        for (int i = 0; i < v; i++)
//            list1.add(i);
//
//        // Bootstrap
//        Collections.shuffle(list1);
//        int x = list1.remove(0);
//        Collections.shuffle(list1);
//        int y = list1.remove(0);
//
//        // Add this edge to hashset
//        Random rnd = new Random();
//        int c = rnd.nextInt(MAX_EDGE_COST) + 1;
//        Edge edge = new Edge(x, y, c);
//        //edges.add(edge);
//        //g.addEdge(x, y);
//        adjMatrix[x][y] = true;
//        adjMatrix[y][x] = true;
//        g.addEdge(edge);
//
//        // Add to processed list
//        list2.add(x);
//        list2.add(y);
//
//        // Ensure a tree is created
//        while (!list1.isEmpty()) {
//            Collections.shuffle(list1);
//            int from = list1.remove(0);
//            Collections.shuffle(list2);
//            int to = list2.get(0);
//            Random r = new Random();
//            int cost = r.nextInt(MAX_EDGE_COST) + 1;
//
//            Edge link = new Edge(from, to, cost);
//            //if (from != to && !(g.edges()).contains(link)) {
//            if (from != to && !adjMatrix[from][to]) {
//                list2.add(from);
//                //edges.add(link);
//                //g.addEdge(from, to);
//                adjMatrix[from][to] = true;
//                adjMatrix[to][from] = true;
//                g.addEdge(link);
//            }
//        }
//
//        // All elements are in list2. Decorate the tree to form a graph
//        long count = g.getE();
//        while (count < e) {
//            //System.out.println(count);
//            Collections.shuffle(list2);
//            //System.out.println("shuffle");
//            Random r = new Random();
//            //int from = list2.get(0);
//            int from = r.nextInt(v);
//            //int to = list2.get(1);
//            int to = r.nextInt(v);
//            //if (from != to && !g.edges().contains(link)) {
//            if (from != to && !adjMatrix[from][to]) {
//                //Random r = new Random();
//                //int cost = r.nextInt(MAX_EDGE_COST) + 1;
//
//                Edge link = new Edge(from, to, r.nextInt(MAX_EDGE_COST) + 1);
//
//                //edges.add(link);
//                adjMatrix[from][to] = true;
//                adjMatrix[to][from] = true;
//                g.addEdge(link);
//                count++;
//                System.out.println(from + "  " + to + "  " + count);
//                //g.addEdge(from, to);
//            }
//        }
//
//        return g;
//    }


    public static boolean isConnectedDFS(Graph g) {
        int v = g.getV();
        Enum<Status>[] status = (Enum<Status>[]) new Enum[v];

        // Vertices are labeled 0 to n-1
        for (int i = 0; i < g.getV(); i++)
            status[i] = Status.UNVISITED;

        for (int i = 0; i < g.getV(); i++)
            visitV(g, i, status);

        for (int i = 0; i < g.getV(); i++)
            if (status[i] != Status.VISITED)
                return false;

        return true;
    }

    private static void visitV(Graph g, int u, Enum<Status>[] status) {
        status[u] = Status.VISITING;

        for (Edge e : g.adjList(u)) {
            int v = e.other(u);
            if (status[v] == Status.UNVISITED)
                visitV(g, v, status);
        }

        status[u] = Status.VISITED;
    }
}
