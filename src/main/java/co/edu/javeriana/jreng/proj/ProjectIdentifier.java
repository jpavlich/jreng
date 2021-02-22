package co.edu.javeriana.jreng.proj;

public class ProjectIdentifier {

    private String groupId;
    private String artifactId;
    private String version;
    private String scope;

    public ProjectIdentifier() {

    }

    public ProjectIdentifier(String groupId, String artifactId, String version, String scope) {
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
    

}
