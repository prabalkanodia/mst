package com.ads;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: prabal
 * Date: 10/21/13
 * Time: 11:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class PrimMST {

    //public enum Scheme {SIMPLE, FIBHEAP};
    boolean isFHeap;
    // Edges in MST
    Edge[] edges;
    // check if vertex is in mst or not
    boolean[] inTree;
    // Cost of MST
    int cost;
    // Min PQ
    //MinPQ minPQ;
    LinkedList<Vertex> q;

    public PrimMST(Graph g, boolean isFHeap) {
        this.isFHeap = isFHeap;
        inTree = new boolean[g.getV()]; // numV
        edges = new Edge[g.getV()]; // numEdges
        //edges = new ArrayList<Edge>();
        cost = 0;
        q = new LinkedList<Vertex>();
    }

    public void mst(Graph g) throws Exception {
        int numV = g.getV();
        Random rnd = new Random();
        int start = rnd.nextInt(numV);
        Vertex r = new Vertex(start);
        r.setKey(0);

        q.add(r);
        for (int i = 0; i < g.getV(); i++) {
            if (i != start)
                q.add(new Vertex(i));
        }

        while (!q.isEmpty()) {
            Vertex u = extractMin(q);
            inTree[u.getV()] = true;
            for (Edge e : g.adjList(u.getV())) {
                int x = e.other(u.getV());
                if (inTree[x]) continue;

                Vertex v = findV(q, x);

                if (v != null && (v.getKey() == -1 || e.getCost() < v.getKey())) {
                    v.setPi(u);
                    v.setKey(e.getCost());

                    // ensure for vertex
                    edges[v.getV()] = e;
                    //edges.add(e);
                }
            }
        }
    }

    public void reset(Graph g, boolean isFHeap) {
        this.isFHeap = isFHeap;
        inTree = new boolean[g.getV()]; // numV
        edges = new Edge[g.getV()]; // numEdges
        //edges = new ArrayList<Edge>();
        cost = 0;
        q = new LinkedList<Vertex>();
    }

//    public Iterable<Edge> edges() {
//        return this.edges;
//    }

    public void printMST() {
        System.out.println(this.cost());
        for (Edge e : edges) {
            if (e != null)
                System.out.println(String.format("%d %d", e.from(), e.to()));
        }
    }

    public Vertex findV(LinkedList<Vertex> q, int v) {
        for (Vertex u : q) {
            if (u.getV() == v)
                return u;
        }

        return null;
    }

    public Vertex extractMin(LinkedList<Vertex> q) throws Exception {
        if (q.size() == 0) throw new Exception("Queue must not be empty while extracting element with min key");

        int minKey = -1;
        //Vertex v = null;
        //int index = 0;
        int rmv = 0;

        if (q.size() >= 1) {
            minKey = q.get(0).getKey();
            //v = q.get(index);
            //index++;
        }

        for (int i = 1; i < q.size(); i++) {
            int key = q.get(i).getKey();
            if (key != -1 && key < minKey) {
                minKey = key;
                rmv = i;
                //v = q.get(i);
            }
        }

        return q.remove(rmv);
        //return v;
    }

    public int cost() {
        int cost = 0;
        for (Edge e : edges) {
            if (e != null)
                cost += e.getCost();
        }

        return cost;
    }

}
