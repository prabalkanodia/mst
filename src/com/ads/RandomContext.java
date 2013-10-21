package com.ads;

/**
 * Created with IntelliJ IDEA.
 * User: prabal
 * Date: 10/18/13
 * Time: 8:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class RandomContext extends Context {

    // no of vertices
    private int nVertices;

    // density in % = ( ratio of edges / total posssible edges ) * 100
    private int density;

    RandomContext(int nVertices, int density) {
        super(true);
        this.nVertices = nVertices;
        this.density = density;
    }

    int getNVertices() {
        return nVertices;
    }

    int getDensity() {
        return density;
    }
}
