package com.ads;

/**
 * Created with IntelliJ IDEA.
 * User: prabal
 * Date: 10/19/13
 * Time: 9:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class Edge implements Comparable<Edge> {
    private int u;
    private int v;
    private int cost;

    public Edge(int u, int v) {
        this(u, v, 0);
    }

    public Edge(int u, int v, int cost) {
        this.u = u < v ? u : v;
        this.v = u + v - this.u;
        this.cost = cost;
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

        if (this.u < that.u) return -1;
        if (this.u > that.u) return +1;
        if (this.v < that.v) return -1;
        if (this.v > that.v) return +1;

        return 0;
    }

    @Override
    public String toString() {
        return String.format("%d %d %d", this.u, this.v, this.cost);
    }
}
