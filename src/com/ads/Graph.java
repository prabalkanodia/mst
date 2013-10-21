package com.ads;

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
    private int e;

    // Adjacency list: array of integer list
    private ArrayList<Integer>[] adj;

    public Graph(int v) {
        this.v = v;
        this.e = 0;
        adj = (ArrayList<Integer>[]) new ArrayList[v];

        for (int i = 0; i < v; i++)
            adj[i] = new ArrayList<Integer>();
    }

    public int getV() {
        return v;
    }

    public int getE() {
        return e;
    }

    public void addEdge(int from, int to) {
        if (from < 0 || from > this.v) throw new IndexOutOfBoundsException("Valid vertex range: 0 - " + from);
        if (to < 0 || to > this.v) throw new IndexOutOfBoundsException("Valid vertex range: 0 - " + to);
        e++;
        adj[from].add(to);
        adj[to].add(from);
    }

    public boolean hasVertex(int v) {
        return adj[v] != null;
    }

    public boolean hasEdge(int from, int to) {
        if (from < 0 || from > this.v) return false;
        if (to < 0 || to > this.v) return false;

        return adj[from].contains(to);
    }

    public Iterable<Integer> adjList(int u) {
        if (u < 0 || u >= this.v) throw new IndexOutOfBoundsException("Valid vertex range: 0 - " + u);
        return adj[u];
    }

    @Override
    public String toString() {
        return null;
    }

    public String printBFSEdges() {
        StringBuffer s = new StringBuffer();
        Queue<Integer> q = new LinkedList<Integer>();
        int numV = this.getV();
        Enum<GraphUtils.STATUS>[] status = (Enum<GraphUtils.STATUS>[]) new Enum[numV];

        for (int i = 0; i < numV; i++) {
            status[i] = GraphUtils.STATUS.UNVISITED;
        }

        // Add 1st element into queue
        q.add(0);
        status[0] = GraphUtils.STATUS.VISITING;

        while (!q.isEmpty()) {
            int u = q.remove();
            for (int v : this.adjList(u)) {
                if (status[v] == GraphUtils.STATUS.UNVISITED) {
                    status[v] = GraphUtils.STATUS.VISITING;
                    q.add(v);
                    s.append(String.format("%d %d\n", u, v));
                }
            }
            status[u] = GraphUtils.STATUS.VISITED;
            //s.append("\n");
        }

        return s.toString();
    }
}
