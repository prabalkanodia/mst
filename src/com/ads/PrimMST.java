package com.ads;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Prim Algorithm implementation using Arrays, Fibonacci Heap and Queue
 * User: prabal
 * Date: 10/21/13
 * Time: 11:34 AM
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
    // Array or list of vertices containing minCost
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
        if (isFHeap)
            fibmst3(g);
        else
            //mst2(g);
            arrmst(g);
    }

    public void fibmst3(Graph g) throws Exception {
        FibHeap<Vertex> fibPQ = new FibHeap<Vertex>();

        int numV = g.getV();
        FibHeap.FibNode<Vertex>[] nodes = new FibHeap.FibNode[numV];

        for (int i = 0; i < numV; i++)
            g.vertices()[i].setKey(GraphUtils.INFINITY);

        Random rnd = new Random();
        int start = rnd.nextInt(numV);
        Vertex r = (g.vertices())[start];
        r.setKey(0);

        nodes[start] = fibPQ.insert(r, 0);
        for (int i = 0; i < g.getV(); i++) {
            if (i != start)
                nodes[i] = fibPQ.insert((g.vertices())[i], GraphUtils.INFINITY);
        }

        while (!fibPQ.isEmpty()) {
            FibHeap.FibNode<Vertex> node = fibPQ.removeMin();
            Vertex u = node.getItem();
            inTree[u.getV()] = true;
            for (Edge e : g.adjList(u.getV())) {
                Vertex v = e.otherV(u);
                if (inTree[v.getV()]) continue;

                if (v != null && nodes[v.getV()] != null && e.getCost() < v.getKey()) {
                    v.setPi(u);
                    v.setKey(e.getCost());

                    fibPQ.decreaseKey(nodes[v.getV()], e.getCost());
                    edges[v.getV()] = e;
                }
            }
        }

    }

    public void fibmst(Graph g) throws Exception {
        FibHeap<Vertex> fibPQ = new FibHeap<Vertex>();

        int numV = g.getV();
        Random rnd = new Random();
        int start = rnd.nextInt(numV);
        Vertex r = (g.vertices())[start];
        r.setKey(0);

        fibPQ.insert(r, 0);
        while(!fibPQ.isEmpty()) {
            Vertex u = (fibPQ.removeMin()).getItem();
            inTree[u.getV()] = true;

            for (Edge e : g.adjList(u.getV())) {
                Vertex v = e.otherV(u);
                if (inTree[v.getV()]) continue;

                if (v != null && (v.getKey() == -1 || e.getCost() < v.getKey())) {
                    v.setPi(u);
                    v.setKey(e.getCost());

                    fibPQ.insert(v, e.getCost());
                    // ensure for vertex
                    edges[v.getV()] = e;
                    //edges.add(e);
                }
            }
        }

    }

    public void arrmst(Graph g) throws Exception {
        int numV = g.getV();
        int[] minCosts = new int[numV];

        Random rnd = new Random();
        int start = rnd.nextInt(numV);
        Vertex r = (g.vertices())[start];
        r.setKey(0);

        minCosts[start] = 0;
        for (int i = 0; i < g.getV(); i++) {
            if (i != start)
                minCosts[i] = GraphUtils.INFINITY;
        }


        for (int i = 0; i < g.getV(); i++) {
            int index = removeMin(minCosts);
            inTree[index] = true;
            for (Edge e : g.adjList(index)) {
                Vertex v = e.otherV(g.vertices()[index]);
                int other = v.getV();
                if (inTree[other]) continue;

                if (v != null && e.getCost() < v.getKey()) {
                    v.setPi(g.vertices()[index]);
                    v.setKey(e.getCost());

                    //minCosts[index] = e.getCost();
                    decreaseKey(minCosts, other, e.getCost());
                    edges[other] = e;
                }
            }
        }
    }

    public void decreaseKey(int[] minCosts, int idx, int newPriority) {
        minCosts[idx] = newPriority;
    }

    public int removeMin(int[] minCosts) {
        int minIdx = 0;
        int minKey = GraphUtils.INFINITY;
        for (int i = 0; i < minCosts.length; i++) {
            if (minCosts[i] < minKey) {
                minIdx = i;
                minKey = minCosts[i];
            }
        }
        // reset the lowest min to infinity
        minCosts[minIdx] = GraphUtils.INFINITY;
        return minIdx;
    }

    public void mstSimple(Graph g) throws Exception {
        int numV = g.getV();
        Random rnd = new Random();
        int start = rnd.nextInt(numV);
        Vertex r = (g.vertices())[start];
        r.setKey(0);

        q.add(r);
        for (int i = 0; i < g.getV(); i++) {
            if (i != start)
                q.add((g.vertices())[i]);
        }

        while (!q.isEmpty()) {
            Vertex u = extractMin(q);
            inTree[u.getV()] = true;
            for (Edge e : g.adjList(u.getV())) {
                Vertex v = e.otherV(u);
                if (inTree[v.getV()]) continue;

                if (v != null && (e.getCost() < v.getKey())) {
                    v.setPi(u);
                    v.setKey(e.getCost());

                    // save in mst edges array
                    edges[v.getV()] = e;
                }
            }
        }
    }

    public void mst2(Graph g) throws Exception {

        int numV = g.getV();
        Random rnd = new Random();
        int start = rnd.nextInt(numV);
        Vertex r = (g.vertices())[start];
        r.setKey(0);

        q.add(r);
        while (!q.isEmpty()) {
            Vertex u = extractMin(q);
            inTree[u.getV()] = true;
            for (Edge e : g.adjList(u.getV())) {
                //int x = e.other(u.getV());
                //if (inTree[x]) continue;

                //Vertex v = findV(q, x);
                Vertex v = e.otherV(u);
                if (inTree[v.getV()]) continue;

                if (v != null && (e.getCost() < v.getKey())) {
                    v.setPi(u);
                    v.setKey(e.getCost());

                    q.add(v);
                    // add to mst edges array
                    edges[v.getV()] = e;
                }
            }
        }
    }

    public void reset(Graph g, boolean isFHeap) {
        // reset parent and minDist from parent for each vertex
        g.resetVertices();
        this.isFHeap = isFHeap;
        inTree = new boolean[g.getV()]; // numV
        edges = new Edge[g.getV()]; // numEdges
        cost = 0;
        q = new LinkedList<Vertex>();
    }

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
        int rmv = 0;

        if (q.size() >= 1) {
            minKey = q.get(0).getKey();
        }

        for (int i = 1; i < q.size(); i++) {
            int key = q.get(i).getKey();
            if (key != -1 && key < minKey) {
                minKey = key;
                rmv = i;
            }
        }

        return q.remove(rmv);
    }

    public long cost() {
        long cost = 0;
        for (Edge e : edges) {
            if (e != null)
                cost += e.getCost();
        }

        return cost;
    }

}
