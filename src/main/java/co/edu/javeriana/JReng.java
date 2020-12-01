package co.edu.javeriana;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.List;

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

/**
 * Some code that uses JavaSymbolSolver.
 */
public class JReng {

    public static void main(String[] args) throws IOException {
        MavenProj proj = new MavenProj(new File("../spring-petclinic/pom.xml"));
        // build(proj);
        System.out.println("Finding jars");
        URLClassLoader cl = proj.getClassLoader();
        TypeSolver typeSolver = new ClassLoaderTypeSolver(cl);
        System.out.println("Found: " + (cl.getURLs().length - 2) + " jars");

        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);
        Collection<File> javas = proj.javas();

        Csv nodes = new Csv();
        nodes.setColumns("id", "type", "subtype");
        Csv connections = new Csv();
        connections.setColumns("src", "dst", "type");

        System.out.println("Extracting static dependencies");
        for (File javaFile : javas) {
            CompilationUnit cu = StaticJavaParser.parse(javaFile);
            List<ClassOrInterfaceDeclaration> classes = cu.findAll(ClassOrInterfaceDeclaration.class);
            for (ClassOrInterfaceDeclaration c : classes) {
                String cname = c.resolve().getQualifiedName();

                nodes.addRow(cname, c.isInterface() ? "I" : "C", "");
                System.out.println(cname);
                List<MethodDeclaration> methods = c.findAll(MethodDeclaration.class);
                for (MethodDeclaration m : methods) {
                    String mname = m.resolve().getQualifiedSignature();
                    nodes.addRow(mname, "M", "");
                    System.out.println("\t" + mname);
                    List<ClassOrInterfaceType> types = m.findAll(ClassOrInterfaceType.class);
                    for (ClassOrInterfaceType t : types) {
                        String tname = "";
                        try {
                            ResolvedReferenceType rt = t.resolve();
                            tname = rt.getQualifiedName();
                            nodes.addRow(tname, rt.getTypeDeclaration().get().isInterface() ? "I" : "C", "");
                        } catch (UnsupportedOperationException e) {
                            tname = t.getNameWithScope();
                            nodes.addRow(tname, t.getMetaModel().getTypeName(), "");
                        } catch (UnsolvedSymbolException e) {
                            System.out.println(e);
                            tname = t.getNameWithScope();
                            nodes.addRow(tname, t.getMetaModel().getTypeName(), "");
                        }
                        System.out.println("\t\t" + tname);
                    }
                    List<MethodCallExpr> calls = m.findAll(MethodCallExpr.class);
                    for (MethodCallExpr call : calls) {
                        ResolvedMethodDeclaration rm = call.resolve();
                        String rmname = rm.getQualifiedSignature();
                        nodes.addRow(rmname, "M", "");
                        System.out.println("\t\t" + rm.getQualifiedSignature());
                    }
                }
            }
        }

        nodes.save(new File("tmp/nodes.csv"));
        connections.save(new File("tmp/connections.csv"));

        // Parse some code
        // CompilationUnit cu = StaticJavaParser.parse("class X { int x() { return 1 +
        // 1.0 - 5; } }");

        // Find all the calculations with two sides:
        // cu.findAll(BinaryExpr.class).forEach(be -> {
        // // Find out what type it has:
        // ResolvedType resolvedType = be.calculateResolvedType();

        // // Show that it's "double" in every case:
        // System.out.println(be.toString() + " is a: " + resolvedType);
        // });
    }

    private static void build(MavenProj proj) {
        System.out.println("Building project " + proj.getPom().getAbsolutePath());
        boolean success = proj.cleanInstall();
        if (!success) {
            return;
        }
    }
}