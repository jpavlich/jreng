package co.edu.javeriana.jreng.proj;

import java.io.File;

public class MavenDep extends ProjDep {
    private static File M2_FOLDER = new File(System.getProperty("user.home") + "/.m2/repository");


    public MavenDep(String groupId, String artifactId, String version, String scope) {
        super(groupId, artifactId, version, scope);
    }


    protected File getDepFolder() {
        return new File(
                M2_FOLDER.getAbsolutePath() + "/" + groupId.replaceAll("\\.", "/") + "/" + artifactId + "/" + version);
    }

}
