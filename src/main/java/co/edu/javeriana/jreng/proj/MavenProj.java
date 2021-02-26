package co.edu.javeriana.jreng.proj;

import java.io.File;

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
        String[] dep = line.strip().split(":");
        return new MavenDep(dep[0], dep[1], dep[3], dep[4]);
    }

}
