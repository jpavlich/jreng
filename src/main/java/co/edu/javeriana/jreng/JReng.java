package co.edu.javeriana.jreng;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ClassLoaderTypeSolver;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import co.edu.javeriana.jreng.dep.DepFinder;
import co.edu.javeriana.jreng.proj.BuildException;
import co.edu.javeriana.jreng.proj.MavenProj;
import co.edu.javeriana.jreng.util.ExcelUtil;

/**
 * Some code that uses JavaSymbolSolver.
 */
public class JReng {

    private String pomPath;
    private boolean cleanInstall;

    public JReng(String pomPath, boolean cleanInstall) {
        this.pomPath = pomPath;
        this.cleanInstall = cleanInstall;
    }

    private MavenProj setup() throws BuildException {
        MavenProj proj = new MavenProj(new File(pomPath));
        if (cleanInstall) {
            proj.cleanInstall();
        }
        System.out.println("Finding jars");
        URLClassLoader cl = proj.getClassLoader();
        TypeSolver typeSolver = new ClassLoaderTypeSolver(cl);
        System.out.println("Found: " + (cl.getURLs().length - 2) + " jars");

        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);
        return proj;
    }

    public boolean process() throws IOException, BuildException {
        MavenProj proj = setup();

        Collection<File> javas = proj.javas();

        DepFinder dep = new DepFinder("");

        for (File javaFile : javas) {
            dep.visit(javaFile);
        }
        // System.out.println(dep.getDeps());

        ExcelUtil xls = new ExcelUtil();
        Workbook wb = new XSSFWorkbook();
        xls.createSheet(wb, "nodes", dep.getNodes(), "id", "type");
        xls.createSheet(wb, "conns", dep.getDeps(), "src", "dst", "type");
        xls.save(wb, "tmp/deps.xlsx");

        return true;
    }

}