package co.edu.javeriana.jreng.proj;

import java.io.File;

public class MavenProj extends ProjectIdentifier implements Project {

    private File pom;

    public MavenProj(File pom) {
        init(getMeta(pom, "groupId"), getMeta(pom, "artifactId"), getMeta(pom, "version"), "");
        this.pom = pom;
    }

    public MavenProj(String groupId, String artifactId, String version, String scope) {
        super(groupId, artifactId, version, scope);
    }

    @Override
    public String getCleanInstallCommand() {
        return "mvn clean install -Dmaven.test.skip=true";
    }

    @Override
    public String getMetaCommand() {
        return "mvn org.apache.maven.plugins:maven-help-plugin:3.2.0:evaluate -Dexpression=project.%s -q -DforceStdout";
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
    public File getDepFolder() {
        File m2Folder = new File(System.getProperty("user.home") + "/.m2/repository");
        return new File(m2Folder.getAbsolutePath() + "/" + getGroupId().replaceAll("\\.", "/") + "/" + getArtifactId()
                + "/" + getVersion());
    }

    @Override
    public Project parse(String line) {
        if (line.strip().isEmpty()) {
            return null;
        }
        String[] dep = line.strip().split(":");
        return new MavenProj(dep[0], dep[1], dep[3], dep[4]);
    }

}
