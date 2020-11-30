package co.edu.javeriana;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class MavenProjResolverTest 
{

    private MavenProj proj;

    @Before
    public void setupPom() {
        proj = new MavenProj(new File("pom.xml"));
    }

    @Test
    public void jarDeps()
    {
        for (File jar : proj.depJars("compile")) {
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
}
