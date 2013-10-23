package com.ads;

public class Main {

    public static void main(String[] args) {
        try {
            Context context = CommandLineParser.validateArgs(args);
            if (context == null) {
                CommandLineParser.printUsage();
            }

            if (context instanceof RandomContext) {
                RandomContext ctxt = (RandomContext)context;
                Graph g = GraphUtils.createConnectedGraph(ctxt.getNVertices(), ctxt.getDensity());
                //Graph g = GraphUtils.createQuickGraph(ctxt.getNVertices(), ctxt.getDensity());
                //Graph g = GraphUtils.createQuickGraph(5, 100);
                //System.out.println(g);
                PrimMST prim = new PrimMST(g, false);
                prim.mst(g);
                prim.printMST();
                //prim.reset(g, false);
                //prim.mst(g);
                //prim.printMST();

            } else if (context instanceof UserContext) {
                UserContext ctxt = (UserContext)context;
                Graph g = GraphUtils.createGraph(ctxt.getFilename());
                System.out.println(g);
            }
        } catch(Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

    }
}
