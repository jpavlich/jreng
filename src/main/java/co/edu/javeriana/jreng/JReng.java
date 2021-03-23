package co.edu.javeriana.jreng;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ClassLoaderTypeSolver;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import co.edu.javeriana.jreng.dep.DepFinder;
import co.edu.javeriana.jreng.dep.DepGraph;
import co.edu.javeriana.jreng.proj.BuildException;
import co.edu.javeriana.jreng.proj.GradleProj;
import co.edu.javeriana.jreng.proj.MavenProj;
import co.edu.javeriana.jreng.proj.Project;
import co.edu.javeriana.jreng.util.ExcelUtil;

public class JReng {

    private String projFile;

    public JReng(String projFile) {
        this.projFile = projFile;
    }

    private Project<?> createProject(File projFile) {
        if (projFile.getName().endsWith("build.gradle")) {
            return new GradleProj(projFile);
        } else if (projFile.getName().endsWith("pom.xml")) {
            return new MavenProj(projFile);
        } else {
            System.err.println("Invalid project file: " + projFile.getName());
            System.exit(1);
            return null;
        }
    }

    private Project<?> setup(boolean cleanInstall) throws BuildException {
        Project<?> proj = createProject(new File(projFile));
        if (cleanInstall) {
            System.out.println("Clean install project " + projFile);
            proj.cleanInstall();
        }
        URLClassLoader cl = proj.getClassLoader();
        TypeSolver typeSolver = new ClassLoaderTypeSolver(cl);
        System.out.println("Found: " + (cl.getURLs().length - 2) + " jars");
        for (URL url : cl.getURLs()) {
            System.out.println(url);
        }
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);
        
        return proj;
    }

    public boolean process(String outFile, boolean cleanInstall) throws IOException, BuildException {
        Project<?> proj = setup(cleanInstall);

        Collection<File> javas = proj.javas();

        DepGraph depGraph = new DepGraph();

        DepFinder dep = new DepFinder(depGraph, "");

        for (File javaFile : javas) {
            dep.visit(javaFile, null);
        }
        

        ExcelUtil xls = new ExcelUtil();
        Workbook wb = new XSSFWorkbook();
        xls.createSheet(wb, "nodes", depGraph.getNodes(), "id", "label", "type");
        xls.createSheet(wb, "conns", depGraph.getDeps(), "source", "target", "label", "type");
        xls.save(wb, outFile);
        System.out.println("Saved results to " + outFile);
        return true;
    }

}