package co.edu.javeriana.jreng.proj;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import co.edu.javeriana.jreng.util.Command;
import co.edu.javeriana.jreng.util.Command.Result;

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

    public File getPom() {
        return pom;
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
        // jars.addAll(getJars());
        for (MavenDep dep : deps()) {
            if (scopeSet.isEmpty() || scopeSet.contains(dep.getScope())) {
                jars.addAll(dep.getJars());
            }
        }
        return jars;
    }

    public URLClassLoader getClassLoader() {
        try {
            // https://stackoverflow.com/a/6219855

            List<URL> urls = new ArrayList<>();
            urls.add(new File(pom.getParentFile(), "target/classes/").toURI().toURL());
            urls.add(new File(pom.getParentFile(), "target/test-classes/").toURI().toURL());
            for (File jarFile : depJars()) {
                urls.add(jarFile.toURI().toURL());
            }

            URLClassLoader cl = new URLClassLoader(urls.toArray(new URL[0]));
            System.out.println(cl.getURLs()[0]);
            System.out.println(cl.getURLs()[1]);
            return cl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public void cleanInstall() throws BuildException {
        Result result = Command.run(MVN_CLEAN_INSTALL, pom.getParentFile());
        if (result.getExitCode() != 0) {
            throw new BuildException(result.toString());
        }
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
