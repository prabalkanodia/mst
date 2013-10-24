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
                System.out.println("edge count: " + g.getE());

                PrimMST prim = new PrimMST(g, false);
                prim.mst(g);
                prim.printMST();
                int w1 = prim.cost();
//                System.out.println("_____VERTICES______");
//                for (int i = 0; i < g.vertices().length; i++)
//                    System.out.println((g.vertices())[i].getV());
                prim.reset(g, false);
                prim.mst(g);
                prim.printMST();
                int w2 = prim.cost();
//                System.out.println("_____VERTICES______");
//                for (int i = 0; i < g.vertices().length; i++)
//                    System.out.println(g.vertices()[i].getV());

                System.out.println("Prim1: " + w1 + " Prim2: " + w2);
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
