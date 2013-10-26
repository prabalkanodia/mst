package com.ads;

/**
 * Main class
 * User: prabal
 * Date: 10/19/13
 * Time: 3:41 PM
 */
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
                System.out.println("edge count: " + g.getE());

                PrimMST prim = new PrimMST(g, true);
                //prim.reset(g, true);
                long start = System.currentTimeMillis();
                prim.mst(g);
                long end = System.currentTimeMillis();
                System.out.println("Time(seconds) <RandomMode using FibHeap>: " + (end - start));
                //prim.printMST();
                long w1 = prim.cost();

                prim.reset(g, false);
                start = System.currentTimeMillis();
                prim.mst(g);
                end = System.currentTimeMillis();
                System.out.println("Time(seconds) <RandomMode using Arrays>: " + (end - start));
                //prim.printMST();
                long w2 = prim.cost();

                System.out.println("PrimFib: " + w1 + " PrimArr: " + w2);
            } else if (context instanceof UserContext) {
                UserContext ctxt = (UserContext)context;
                Graph g = GraphUtils.createGraph(ctxt.getFilename());

                if (ctxt.IsFHeap()) {
                    PrimMST prim = new PrimMST(g, ctxt.IsFHeap());
                    System.out.println("MST using Fiboannci Heap");
                    prim.mst(g);
                    prim.printMST();
                    //long w1 = prim.cost();
                    //System.out.println("PrimFib: " + w1);
                } else {
                    PrimMST prim = new PrimMST(g, ctxt.IsFHeap());
                    System.out.println("MST using Array");
                    prim.mst(g);
                    prim.printMST();
                    //long w2 = prim.cost();
                    //System.out.println("PrimArray: " + w2);
                }
            }
        } catch(Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

    }
}
