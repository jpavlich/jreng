
package co.edu.javeriana.jreng;

import java.io.IOException;

import co.edu.javeriana.jreng.proj.BuildException;

/**
 * Main
 */
public class Main {

    public static void main(String[] args) throws IOException, BuildException {
        if (args.length < 2) {
            System.out.println("arguments: <path to pom.xml> <output file> [-c]");
            System.exit(1);
        }
        boolean cleanInstall = false;
        if (args.length > 2 && args[2].equals("-c")) {
            cleanInstall = true;
        }
        new JReng(args[0]).process(args[1], cleanInstall);
    }
}