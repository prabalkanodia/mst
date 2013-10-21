package com.ads;

/**
 * Created with IntelliJ IDEA.
 * User: prabal
 * Date: 10/20/13
 * Time: 11:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class Vertex {
    // vertex no.
    private int v;
    // distance from source
    private int key;
    // parent
    private int pi;

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

    public int getPi() {
        return pi;
    }

    public void setPi(int pi) {
        this.pi = pi;
    }
}
