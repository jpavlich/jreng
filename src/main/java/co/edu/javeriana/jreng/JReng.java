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

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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

        // DataFrame<String> connectionsDf = new DataFrame<>("src", "dst", "type");
        Set<List<String>> nodes = new HashSet<>();
        Set<List<String>> connections = new HashSet<>();

        System.out.println("Extracting static dependencies");
        for (File javaFile : javas) {
            CompilationUnit cu = StaticJavaParser.parse(javaFile);
            List<ClassOrInterfaceDeclaration> classes = cu.findAll(ClassOrInterfaceDeclaration.class);
            for (ClassOrInterfaceDeclaration c : classes) {
                String cname = c.resolve().getQualifiedName();

                nodes.add(Arrays.asList(cname, c.isInterface() ? "I" : "C", ""));
                System.out.println(cname);
                List<MethodDeclaration> methods = c.getMethods();
                for (MethodDeclaration m : methods) {
                    String mname = m.resolve().getQualifiedSignature();
                    nodes.add(Arrays.asList(mname, "M", ""));
                    System.out.println("\t" + mname);
                    List<ClassOrInterfaceType> types = m.findAll(ClassOrInterfaceType.class);
                    for (ClassOrInterfaceType t : types) {
                        String tname = "";
                        try {
                            ResolvedReferenceType rt = t.resolve();
                            tname = rt.getQualifiedName();
                            nodes.add(
                                    Arrays.asList(tname, rt.getTypeDeclaration().get().isInterface() ? "I" : "C", ""));
                        } catch (UnsupportedOperationException e) {
                            tname = t.getNameWithScope();
                            nodes.add(Arrays.asList(tname, t.getMetaModel().getTypeName(), ""));
                        } catch (UnsolvedSymbolException e) {
                            System.out.println(e);
                            tname = t.getNameWithScope();
                            nodes.add(Arrays.asList(tname, t.getMetaModel().getTypeName(), ""));
                        }
                        System.out.println("\t\t" + tname);
                    }
                    List<MethodCallExpr> calls = m.findAll(MethodCallExpr.class);
                    for (MethodCallExpr call : calls) {
                        ResolvedMethodDeclaration rm = call.resolve();
                        String rmname = rm.getQualifiedSignature();
                        nodes.add(Arrays.asList(rmname, "M", ""));
                        System.out.println("\t\t" + rm.getQualifiedSignature());
                    }
                }
            }
        }

        ExcelUtil xls = new ExcelUtil();
        Workbook wb = new XSSFWorkbook();

        Workbook nodesWb = xls.createSheet(wb, "nodes", nodes, "id", "type");
        xls.save(nodesWb, "tmp/nodes.xlsx");

        // Workbook connectionsWb = xls.createSheet("nodes", nodes, "id", "type");
        // xls.save(connectionsWb, "tmp/connections.xlsx");

        return true;
    }


}