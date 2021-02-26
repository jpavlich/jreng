package co.edu.javeriana.jreng.proj;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

public abstract class ProjDep {

    private String groupId;
    private String artifactId;
    private String version;
    private String scope;

    public ProjDep() {

    }

    public ProjDep(String groupId, String artifactId, String version, String scope) {
        init(groupId, artifactId, version, scope);
    }

    public void init(String groupId, String artifactId, String version, String scope) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.scope = scope;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

    public String getScope() {
        return scope;
    }

    
    public Collection<File> getJars() {
        return FileUtils.listFiles(getDepFolder(), new String[] { "jar" }, true);
    }

    protected abstract File getDepFolder();

}
