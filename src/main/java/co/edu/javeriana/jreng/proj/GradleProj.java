package co.edu.javeriana.jreng.proj;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

public class GradleProj implements Project<GradleDep> {

    private File gradleBuildFile;

    public GradleProj(File gradleBuildFile) {
        this.gradleBuildFile = gradleBuildFile;
    }

    @Override
    public String getCleanInstallCommand() {
        return "gradle clean build -Dorg.gradle.java.home=/usr/lib/jvm/java-8-openjdk -x test";
    }

    @Override
    public String getDepsCommand() {
        return new File("./scripts/gradle_deps.sh").getAbsolutePath() + " %s";
    }

    @Override
    public File getProjFile() {
        return gradleBuildFile;
    }

    @Override
    public GradleDep parse(String line) {
        line = line.strip();
        if (line.isEmpty() || line.endsWith("(n)")) {
            return null;
        }
        if (line.endsWith("(c)")) {
            line = line.substring(0, line.length() - 3).strip();
        }
        String[] dep = line.split(":");
        int last = dep.length - 1;
        return new GradleDep(dep[0], dep[1], dep[last], "compile");
    }

    @Override
    public List<URL> getBuildFolderURLs() {
        List<URL> urls = new ArrayList<>();
        try {
            urls.add(new File(getProjFile().getParentFile(), "build/classes/java/main/").toURI().toURL());
            urls.add(new File(getProjFile().getParentFile(), "build/classes/java/test/").toURI().toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return urls;
    }
}
