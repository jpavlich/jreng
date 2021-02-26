package co.edu.javeriana.jreng.proj;

import java.io.File;

public class GradleDep extends ProjDep {

    public GradleDep(String groupId, String artifactId, String version, String scope) {
        super(groupId, artifactId, version, scope);
    }

    @Override
    protected File getDepFolder() {
        File m2Folder = new File(System.getProperty("user.home") + "/.gradle/caches/modules-2/files-2.1");
        return new File(m2Folder.getAbsolutePath() + "/" + getGroupId() + "/" + getArtifactId()
                + "/" + getVersion());
    }

}
