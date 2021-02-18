package co.edu.javeriana.jreng.proj;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

public abstract class ProjDep {
    protected String groupId;
    protected String artifactId;
    protected String version;
    protected String scope;

    public ProjDep(String groupId, String artifactId, String version, String scope) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.scope = scope;
    }

    public Collection<File> getJars() {
        return FileUtils.listFiles(getDepFolder(), new String[] { "jar" }, true);
    }

    
    public String getScope() {
        return scope;
    }
    
    protected abstract File getDepFolder();
    
    @Override
    public String toString() {
        return artifactId + ":" + groupId + ":" + scope + ":" + version;
    }

}
