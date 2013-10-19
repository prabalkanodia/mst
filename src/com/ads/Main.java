package com.ads;

public class Main {

    public static void main(String[] args) {
        Context context = CommandLineParser.validateArgs(args);
        if (context == null) {
            CommandLineParser.printUsage();
        }
    }
}
