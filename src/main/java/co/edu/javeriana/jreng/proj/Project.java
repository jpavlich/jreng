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
import java.util.Set;

import org.apache.commons.io.FileUtils;

import co.edu.javeriana.jreng.util.Command;
import co.edu.javeriana.jreng.util.Command.Result;

/**
 * ProjectDependency
 */
public interface Project {

    static String SOURCE_FOLDER = "/src";

    default String getMeta(File projFile, String attribute) {
        return Command.run(String.format(getMetaCommand(), attribute), projFile.getParentFile()).getOutput().get(0);
    }

    default Collection<File> javas() {
        return FileUtils.listFiles(getSourceFolder(), new String[] { "java" }, true);
    }

    default Collection<File> getJars() {
        return FileUtils.listFiles(getDepFolder(), new String[] { "jar" }, true);
    }

    default List<File> depJars(String... scopes) {
        Set<String> scopeSet = new HashSet<>();
        scopeSet.addAll(Arrays.asList(scopes));

        List<File> jars = new ArrayList<>();
        // jars.addAll(getJars());
        for (Project dep : deps()) {
            if (scopeSet.isEmpty() || scopeSet.contains(dep.getScope())) {
                jars.addAll(dep.getJars());
            }
        }
        return jars;
    }

    String getScope();

    default URLClassLoader getClassLoader() {
        try {
            // https://stackoverflow.com/a/6219855

            List<URL> urls = new ArrayList<>();
            urls.add(new File(getProjFile().getParentFile(), "target/classes/").toURI().toURL());
            urls.add(new File(getProjFile().getParentFile(), "target/test-classes/").toURI().toURL());
            for (File jarFile : depJars()) {
                urls.add(jarFile.toURI().toURL());
            }

            URLClassLoader cl = new URLClassLoader(urls.toArray(new URL[0]));
            // System.out.println(cl.getURLs()[0]);
            // System.out.println(cl.getURLs()[1]);
            return cl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

    }

    default List<Project> deps() {
        List<Project> deps = new ArrayList<>();
        Result result = Command.run(String.format(getDepsCommand(), getProjFile().getAbsolutePath()),
                getProjFile().getParentFile());
        for (String line : result.getOutput().subList(2, result.getOutput().size())) {
            Project dep = parse(line);
            if (dep!=null) {
                deps.add(dep);
            }
        }
        return deps;
    }

    default void cleanInstall() throws BuildException {
        Result result = Command.run(getCleanInstallCommand(), getProjFile().getParentFile());
        if (result.getExitCode() != 0) {
            throw new BuildException(result.toString());
        }
    }

    default File getSourceFolder() {
        return new File(getProjFile().getAbsoluteFile().getParent() + SOURCE_FOLDER);
    }

    // void init(File projFile);

    String getCleanInstallCommand();

    String getMetaCommand();

    String getDepsCommand();

    File getProjFile();

    File getDepFolder();

    Project parse(String line);

}