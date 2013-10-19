package com.ads;

/**
 * Created with IntelliJ IDEA.
 * User: prabal
 * Date: 10/18/13
 * Time: 8:37 PM
 * To change this template use File | Context | File Templates.
 */
public class UserContext extends Context {

    // Fibonacci Heap scheme or Simple scheme
    boolean isFHeap;

    // File-name
    String filename;

    UserContext(boolean isFHeap, String filename) {
        super(false);
        this.isFHeap = isFHeap;
        this.filename = filename;
    }

    boolean IsRandomMode() {
        return isRandomMode;
    }

    boolean IsFHeap() {
        return isFHeap;
    }

    String getFilename() {
        // assert isRandomMode = false; throw IllegalArgumentException("file-name allowed only in user-input mode");
        return filename;
    }
}
