package com.ads;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: prabal
 * Date: 10/18/13
 * Time: 8:05 PM
 * To change this template use File | Context | File Templates.
 */
public class CommandLineParser {

    public static Context validateArgs(String[] args) {
        if (args == null || args.length < 2 || args.length > 3)
            return null;

        Context context = null;

        String option = args[0].trim();
        if (option.equals("-r")) {
            int v;
            int d;

            try {
                v = Integer.parseInt(args[1].trim());
                d = Integer.parseInt(args[2].trim());

                context = new RandomContext(v, d);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format: 'n' and 'd' must be numbers");
            }

        } else if (option.equals("-s") || option.equals("-f")) {
            boolean isFHeap = option.equals("-f");
            String filename = args[1].trim();
            File file = new File(filename);
            if (!file.isFile())
                context = new UserContext(isFHeap, filename);
            else
                System.out.println("File does not exist: " + filename);
        }

        return context;
    }

    public static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  mst -r n d");
        System.out.println("  mst -s filename");
        System.out.println("  mst -f filename");
    }
}
