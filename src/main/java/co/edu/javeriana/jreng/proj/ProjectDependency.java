package co.edu.javeriana.jreng.proj;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import co.edu.javeriana.jreng.util.Command;

/**
 * ProjectDependency
 */
public interface ProjectDependency {

    default String getMetadata(String attribute) {
        return Command.run(String.format(getMetaCommand(), attribute), getProjFile().getParentFile()).getOutput()
                .get(0);
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
        for (ProjectDependency dep : deps()) {
            if (scopeSet.isEmpty() || scopeSet.contains(dep.getScope())) {
                jars.addAll(dep.getJars());
            }
        }
        return jars;
    }

    String getArtifactId();

    String getGroupId();

    String getScope();

    String getVersion();

    File getDepFolder();

    String getMetaCommand();

    String getDepCmd();

    File getProjFile();

    List<ProjectDependency> deps();

    File getSourceFolder();

}