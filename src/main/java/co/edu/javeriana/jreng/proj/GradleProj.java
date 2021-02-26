package co.edu.javeriana.jreng.proj;

import java.io.File;

public class GradleProj implements Project<GradleDep> {

    private File gradleBuildFile;

    public GradleProj(File gradleBuildFile) {
        this.gradleBuildFile = gradleBuildFile;
    }

    @Override
    public String getCleanInstallCommand() {
        return "gradle clean build -x test";
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
        if (line.strip().isEmpty()) {
            return null;
        }
        System.out.println(line);
        String[] dep = line.strip().split(":");
        return new GradleDep(dep[0], dep[1], dep[2], "compile");
    }
}
