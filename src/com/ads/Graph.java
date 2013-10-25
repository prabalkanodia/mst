package com.ads;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: prabal
 * Date: 10/18/13
 * Time: 8:05 PM
 * To change this template use File | Context | File Templates.
 */
public class Graph {
    // no of vertices
    private int v;

    // no of edges
    private long e;

    // Vertex array
    private Vertex[] vertices;

    // Adjacency list: array of edge list
    private ArrayList<Edge>[] adj;

//    private void init(int v) {
//        this.v = v;
//        this.e = 0;
//        adj = (ArrayList<Integer>[]) new ArrayList[v];
//
//        for (int i = 0; i < v; i++)
//            adj[i] = new ArrayList<Integer>();
//    }


    public Graph(int v) {
//        init(v);
        this.v = v;
        this.e = 0;

        vertices = new Vertex[v];
        for (int i = 0; i < v; i++) {
            vertices[i] = new Vertex(i);
        }

        adj = (ArrayList<Edge>[]) new ArrayList[v];
        for (int i = 0; i < v; i++)
            adj[i] = new ArrayList<Edge>();
    }

    public Vertex[] vertices() {
        return vertices;
    }

    public void resetVertices() {
        for (Vertex v : vertices) {
            v.setKey(GraphUtils.INFINITY);
            v.setPi(null);
        }

    }

    public int getV() {
        return v;
    }

    public long getE() {
        return e;
    }

    public void addEdge(Edge edge) {
        if (edge.from() < 0 || edge.from() > this.v)
            throw new IndexOutOfBoundsException("Valid vertex range: 0 - " + edge.from());
        if (edge.to() < 0 || edge.to() > this.v)
            throw new IndexOutOfBoundsException("Valid vertex range: 0 - " + edge.to());
        e++;
        adj[edge.from()].add(edge);
        adj[edge.to()].add(edge);
    }

//    public void addEdge(int from, int to) {
//        if (from < 0 || from > this.v) throw new IndexOutOfBoundsException("Valid vertex range: 0 - " + from);
//        if (to < 0 || to > this.v) throw new IndexOutOfBoundsException("Valid vertex range: 0 - " + to);
//        e++;
//        adj[from].add(to);
//        adj[to].add(from);
//    }

    public boolean hasVertex(int v) {
        return adj[v] != null;
    }

    public boolean hasEdge(int from, int to) {
        if (from < 0 || from > this.v) return false;
        if (to < 0 || to > this.v) return false;

        return adj[from].contains(to);
    }

    public Iterable<Edge> adjList(int u) {
        if (u < 0 || u >= this.v) throw new IndexOutOfBoundsException("Valid vertex range: 0 - " + u);
        return adj[u];
    }

//    public Iterable<Integer> adjList(int u) {
//        if (u < 0 || u >= this.v) throw new IndexOutOfBoundsException("Valid vertex range: 0 - " + u);
//        return adj[u];
//    }

    @Override
    public String toString() {
        int numV = getV();
        StringBuilder s = new StringBuilder();

        boolean[][] adjMatrix = new boolean[numV][numV];
        for (int i = 0; i < numV; i++)
            for (int j = 0; j < numV; j++)
                adjMatrix[i][j] = false;

        //HashSet<Edge> edges = new HashSet<Edge>();
        Queue<Integer> q = new LinkedList<Integer>();
        q.add(0);

        while (!q.isEmpty()) {
            int u = q.remove();
            for (Edge e : this.adjList(u)) {
                //Edge e = new Edge(u, v);
                //if (!edges.contains(e)) {
                int from = e.from();
                int to = e.to();
                if (!adjMatrix[e.from()][e.to()]) {
                    q.add(e.other(u));
                    //edges.add(e);
                    adjMatrix[from][to] = true;
                    adjMatrix[to][from] = true;
                    s.append(String.format("%d %d\n", e.from(), e.to()));
                }
            }
        }

        return s.toString();
    }

    public String printBFSEdges() {
        StringBuffer s = new StringBuffer();
        Queue<Integer> q = new LinkedList<Integer>();
        int numV = this.getV();
        Enum<GraphUtils.Status>[] status = (Enum<GraphUtils.Status>[]) new Enum[numV];

        for (int i = 0; i < numV; i++) {
            status[i] = GraphUtils.Status.UNVISITED;
        }

        // Add 1st element into queue
        q.add(0);
        status[0] = GraphUtils.Status.VISITING;

        while (!q.isEmpty()) {
            int u = q.remove();
            for (Edge e : this.adjList(u)) {
                int v = e.other(u);
                if (status[v] == GraphUtils.Status.UNVISITED) {
                    status[v] = GraphUtils.Status.VISITING;
                    q.add(v);
                    s.append(String.format("%d %d\n", u, v));
                }
            }
            status[u] = GraphUtils.Status.VISITED;
            //s.append("\n");
        }

        return s.toString();
    }

//    public HashSet<Edge> edges() {
//        HashSet<Edge> links = new HashSet<Edge>();
//        for (int u = 0; u < getV(); u++) {
//            for (Edge e : adjList(u)) {
//                if (e.other(u) > u)
//                    links.add(e);
//            }
//        }
//
//        return links;
//    }
}
