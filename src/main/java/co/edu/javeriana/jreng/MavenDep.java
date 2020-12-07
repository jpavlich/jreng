package co.edu.javeriana.jreng;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

public class MavenDep {
    private static File M2_FOLDER = new File(System.getProperty("user.home") + "/.m2/repository");

    private String groupId;
    private String artifactId;
    private String version;
    private String scope;

    public MavenDep(String groupId, String artifactId, String version, String scope) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.scope = scope;
    }

    public Collection<File> getJars() {
        return FileUtils.listFiles(getDepFolder(), new String[] { "jar" }, true);
    }

    private File getDepFolder() {
        return new File(
                M2_FOLDER.getAbsolutePath() + "/" + groupId.replaceAll("\\.", "/") + "/" + artifactId + "/" + version);
    }

    public String getScope() {
        return scope;
    }

    @Override
    public String toString() {
        return artifactId + ":" + groupId + ":" + scope + ":" + version;
    }

}
