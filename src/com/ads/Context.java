package com.ads;

/**
 * Settings stored by parsing command line arguments
 * User: prabal
 * Date: 10/18/13
 * Time: 8:21 PM
 */
public class Context {

    // Random mode or user-input mode
    boolean isRandomMode;

    Context(boolean isRandomMode) {
        this.isRandomMode = isRandomMode;
    }
}
