package co.edu.javeriana.jreng.maven;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import co.edu.javeriana.jreng.proj.MavenDep;
import co.edu.javeriana.jreng.proj.MavenProj;

public class MavenProjTest {

    private MavenProj proj;

    @Before
    public void setupPom() {
        proj = new MavenProj(new File("../spring-petclinic/pom.xml"));
    }

    @Test
    public void jarDeps() {
        for (File jar : proj.depJars()) {
            assertTrue(jar.exists());
            System.out.println(jar);
        }
    }

    @Test
    public void deps() {
        System.out.println(proj.deps());
    }

    @Test
    public void javas() {
        System.out.println(proj.javas());
    }

    @Test
    public void classLoader() throws ClassNotFoundException {
        ClassLoader cl = proj.getClassLoader();
        System.out.println(cl.loadClass("org.springframework.samples.petclinic.owner.PetRepository"));
        System.out.println(cl.loadClass("javax.cache.configuration.Configuration"));
        
    }

    @Test
    public void resolveJavaxCache() {
        MavenDep dep = new MavenDep("javax.cache", "cache-api", "1.1.1", "compile");
        for (File jar : dep.getJars()) {
            System.out.println(jar);
            assertTrue(jar.exists());
        }
    }
}
