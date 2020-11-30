package co.edu.javeriana;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import co.edu.javeriana.Command.Result;

public class MavenProj extends MavenDep {

    private static String MVN_DEP_CMD = "mvn -q dependency:list -DoutputFile=/dev/stdout -f \"%s\"";
    private static String MVN_CLEAN_INSTALL = "mvn clean install -Dmaven.test.skip=true";
    private static String MVN_SOURCE_FOLDER = "/src";

    private static String MVN_META_CMD = "mvn org.apache.maven.plugins:maven-help-plugin:3.2.0:evaluate -Dexpression=project.%s -q -DforceStdout";

    private File pom;

    public MavenProj(File pom) {
        super(getMeta(pom, "groupId"), getMeta(pom, "artifactId"), getMeta(pom, "version"), "");
        this.pom = pom;
    }

    private static String getMeta(File pom, String meta) {
        return Command.run(String.format(MVN_META_CMD, meta), pom.getParentFile()).getOutput().get(0);
    }

    public List<MavenDep> deps() {
        List<MavenDep> deps = new ArrayList<>();
        Result result = Command.run(String.format(MVN_DEP_CMD, pom.getAbsolutePath()), pom.getParentFile());
        for (String line : result.getOutput().subList(2, result.getOutput().size())) {
            Optional<MavenDep> dep = parse(line);
            if (dep.isPresent()) {
                deps.add(dep.get());
            }
        }
        return deps;
    }

    public Collection<File> javas() {
        return FileUtils.listFiles(getSourceFolder(), new String[] { "java" }, true);
    }

    public List<File> depJars(String... scopes) {
        Set<String> scopeSet = new HashSet<>();
        scopeSet.addAll(Arrays.asList(scopes));

        List<File> jars = new ArrayList<>();
        jars.addAll(getJars());
        for (MavenDep dep : deps()) {
            if (scopeSet.isEmpty() || scopeSet.contains(dep.getScope())) {
                jars.addAll(dep.getJars());
            }
        }
        return jars;
    }

    public boolean cleanInstall() {
        Result result = Command.run(MVN_CLEAN_INSTALL, pom.getParentFile());
        System.out.println(result);
        return result.getExitCode() == 0;
    }

    private Optional<MavenDep> parse(String line) {
        if (line.strip().isEmpty()) {
            return Optional.ofNullable(null);
        }
        String[] dep = line.strip().split(":");
        return Optional.ofNullable(new MavenDep(dep[0], dep[1], dep[3], dep[4]));
    }

    public File getSourceFolder() {
        return new File(pom.getAbsoluteFile().getParent() + MVN_SOURCE_FOLDER);
    }

}
