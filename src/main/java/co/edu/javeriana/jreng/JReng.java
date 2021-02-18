package co.edu.javeriana.jreng;

import java.io.File;
import java.io.IOException;
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
import co.edu.javeriana.jreng.proj.MavenProj;
import co.edu.javeriana.jreng.util.ExcelUtil;

public class JReng {

    private String pomPath;

    public JReng(String pomPath) {
        this.pomPath = pomPath;
    }

    private MavenProj setup(boolean cleanInstall) throws BuildException {
        MavenProj proj = new MavenProj(new File(pomPath));
        if (cleanInstall) {
            System.out.println("Clean install project " + pomPath);
            proj.cleanInstall();
        }
        URLClassLoader cl = proj.getClassLoader();
        TypeSolver typeSolver = new ClassLoaderTypeSolver(cl);
        System.out.println("Found: " + (cl.getURLs().length - 2) + " jars");

        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);
        return proj;
    }

    public boolean process(String outFile, boolean cleanInstall) throws IOException, BuildException {
        MavenProj proj = setup(cleanInstall);

        Collection<File> javas = proj.javas();

        DepGraph depGraph = new DepGraph();

        DepFinder dep = new DepFinder(depGraph, "");

        for (File javaFile : javas) {
            dep.visit(javaFile, null);
        }

        ExcelUtil xls = new ExcelUtil();
        Workbook wb = new XSSFWorkbook();
        xls.createSheet(wb, "nodes", depGraph.getNodes(), "id", "type");
        xls.createSheet(wb, "conns", depGraph.getDeps(), "src", "dst", "type");
        xls.save(wb, outFile);
        System.out.println("Saved results to " + outFile);
        return true;
    }

}