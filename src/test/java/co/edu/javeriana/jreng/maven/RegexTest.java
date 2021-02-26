package co.edu.javeriana.jreng.maven;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class RegexTest {
    Pattern idPat = Pattern.compile(".*\\.(\\w+(\\(.*\\))?)");

    @Test
    public void testIdRegex() {
        Matcher m = idPat.matcher("co.edu.javeriana.jreng.testproj1.C2.c2_m1");
        System.out.println(m.matches());
        System.out.println(m.group(1));
        System.out.println(m.group(2));
        
    }
}
