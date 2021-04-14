package co.edu.javeriana.jreng.proj;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MavenProj implements Project<MavenDep> {

    private File pom;

    public MavenProj(File pom) {
        this.pom = pom;
    }

    @Override
    public String getCleanInstallCommand() {
        return "mvn clean install -Dmaven.test.skip=true";
    }

    @Override
    public String getDepsCommand() {
        return "mvn -q dependency:list -DoutputFile=/dev/stdout -f \"%s\"";
    }

    @Override
    public File getProjFile() {
        return pom;
    }

    @Override
    public MavenDep parse(String line) {
        if (line.strip().isEmpty()) {
            return null;
        }
        System.out.println(line);
        String[] dep = line.strip().split(":");
        if (dep.length < 5) {
            return null;
        }
        if (dep[3].equals("tests")) {
            return new MavenDep(dep[0], dep[1], dep[4], dep[5]);
        } else {
            return new MavenDep(dep[0], dep[1], dep[3], dep[4]);
        }
    }

    @Override
    public List<URL> getBuildFolderURLs() {
        List<URL> urls = new ArrayList<>();
        try {
            urls.add(new File(getProjFile().getParentFile(), "target/classes/").toURI().toURL());
            urls.add(new File(getProjFile().getParentFile(), "target/test-classes/").toURI().toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return urls;
    }

}
