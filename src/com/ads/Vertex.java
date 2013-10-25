package com.ads;

/**
 * Vertex node
 * User: prabal
 * Date: 10/20/13
 * Time: 11:08 AM
 */
public class Vertex {
    // vertex no.
    private int v;
    // distance from source
    private int key;
    // parent
    private Vertex pi;

    public Vertex(int v) {
        this.v = v;
        this.key = GraphUtils.INFINITY;
        this.pi = null;

    }

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public Vertex getPi() {
        return pi;
    }

    public void setPi(Vertex pi) {
        this.pi = pi;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Vertex))
            return false;
        Vertex v  = (Vertex)o;
        return this.getV() == v.getV();
    }

    @Override
    public int hashCode() {
        return String.format("%d", getV()).hashCode();
    }
}
