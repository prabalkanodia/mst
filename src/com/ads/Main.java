package com.ads;

public class Main {

    public static void main(String[] args) {
        Context context = CommandLineParser.validateArgs(args);
        if (context == null) {
            CommandLineParser.printUsage();
        }

        if (context instanceof RandomContext) {
            RandomContext ctxt = (RandomContext)context;
            Graph g = GraphUtils.createConnectedGraph(ctxt.getNVertices(), ctxt.getDensity());
            //Graph g = GraphUtils.createQuickGraph(ctxt.getNVertices(), ctxt.getDensity());
            //Graph g = GraphUtils.createQuickGraph(5, 100);
            System.out.println(g);

        } else if (context instanceof UserContext) {
            UserContext ctxt = (UserContext)context;
            GraphUtils.createGraph(5, 100);
        }

    }
}
