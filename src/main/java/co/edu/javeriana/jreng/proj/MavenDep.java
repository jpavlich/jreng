package co.edu.javeriana.jreng.proj;

import java.io.File;

public class MavenDep extends ProjDep {

    public MavenDep(String groupId, String artifactId, String version, String scope) {
        super(groupId, artifactId, version, scope);
    }

    @Override
    protected File getDepFolder() {
        File m2Folder = new File(System.getProperty("user.home") + "/.m2/repository");
        return new File(m2Folder.getAbsolutePath() + "/" + getGroupId().replaceAll("\\.", "/") + "/" + getArtifactId()
                + "/" + getVersion());
    }

}
