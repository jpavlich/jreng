package co.edu.javeriana.jreng.maven;

import org.junit.Test;

import co.edu.javeriana.jreng.dep.Catalog;

public class CatalogTest {
    Catalog cat = new Catalog();

    @Test
    public void shortNameTest() {
        String n = cat.shortName(
                "co.edu.javeriana.jreng.testproj1.C2.c2_m1(co.edu.javeriana.jreng.testproj1.I2, co.edu.javeriana.jreng.testproj1.J1)");
        System.out.println(n);
    }
    
    @Test
    public void shortNameTest2() {
        String n = cat.shortName(
                "co.edu.javeriana.jreng.testproj1.App.main(java.lang.String[])");
        System.out.println(n);
    }
}
