package com.ads;

import java.util.ArrayList;

/**
 * Edge in a graph
 * User: prabal
 * Date: 10/19/13
 * Time: 9:54 PM
 */
public class Edge implements Comparable<Edge> {

    private Vertex u;
    private Vertex v;
    private int cost;

    public Edge(Vertex u, Vertex v) {
        this(u, v, 0);
    }

    public Edge(int a, int b, int cost) {
        if (a < b) {
            this.u = new Vertex(a);
            this.v = new Vertex(b);
        } else {
            this.u = new Vertex(b);
            this.v = new Vertex(a);
        }
        this.cost = cost;
    }

    public Edge(Vertex u, Vertex v, int cost) {
        if (u.getV() < v.getV()) {
            this.u = u;
            this.v = v;
        } else {
            this.u = v;
            this.v = u;
        }
        this.cost = cost;
    }

    public int from() {
        return this.u.getV();
    }

    public int to() {
        return this.v.getV();
    }

    public int getCost() {
        return this.cost;
    }

    public int other(int u) {
        return this.u.getV() == u ? this.v.getV() : this.u.getV();
    }

    public Vertex otherV(Vertex u) {
        return this.u.getV() == u.getV() ? this.v : this.u;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Edge))
            return false;
        Edge that = (Edge)o;
        return (this.u == that.u && this.v == that.v);
    }

    @Override
    public int hashCode() {
        return String.format("%d %d", this.u, this.v).hashCode();
    }

    @Override
    public int compareTo(Edge that) {
        //if (this.cost < that.cost) return -1;
        //if (this.cost > that.cost) return +1;

        if (this.u.getV() < that.u.getV()) return -1;
        if (this.u.getV() > that.u.getV()) return +1;
        if (this.v.getV() < that.v.getV()) return -1;
        if (this.v.getV() > that.v.getV()) return +1;

        return 0;
    }

    @Override
    public String toString() {
        return String.format("%d %d", this.u, this.v, this.cost);
    }
}
