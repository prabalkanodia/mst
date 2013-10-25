package com.ads;

/**
 * Settings for RandomMode
 * User: prabal
 * Date: 10/18/13
 * Time: 8:40 PM
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
