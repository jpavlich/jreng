
package co.edu.javeriana.jreng;

import java.io.IOException;

import co.edu.javeriana.jreng.proj.BuildException;

/**
 * Main
 */
public class Main {

    public static void main(String[] args) throws IOException, BuildException {
        new JReng("../spring-petclinic/pom.xml", false).process();
    }
}