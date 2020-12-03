package co.edu.javeriana;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
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

import joinery.DataFrame;

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

        // Csv nodes = new Csv();
        // nodes.setColumns("id", "type", "subtype");
        // Csv connections = new Csv();
        // connections.setColumns("src", "dst", "type");

        // List<List<String>> nodes = new ArrayList<>();
        // nodes.add(Arrays.asList("id", "type", "subtype"));
        // List<List<String>> connections = new ArrayList<>();
        // connections.add(Arrays.asList("src", "dst", "type"));

        DataFrame<String> nodesDf = new DataFrame<>("id", "type", "subtype");
        DataFrame<String> connectionsDf = new DataFrame<>("src", "dst", "type");

        int i = 0;
        System.out.println("Extracting static dependencies");
        for (File javaFile : javas) {
            CompilationUnit cu = StaticJavaParser.parse(javaFile);
            List<ClassOrInterfaceDeclaration> classes = cu.findAll(ClassOrInterfaceDeclaration.class);
            for (ClassOrInterfaceDeclaration c : classes) {
                String cname = c.resolve().getQualifiedName();

                nodesDf.append(i++, Arrays.asList(cname, c.isInterface() ? "I" : "C", ""));
                System.out.println(cname);
                List<MethodDeclaration> methods = c.getMethods();
                for (MethodDeclaration m : methods) {
                    String mname = m.resolve().getQualifiedSignature();
                    nodesDf.append(i++, Arrays.asList(mname, "M", ""));
                    System.out.println("\t" + mname);
                    List<ClassOrInterfaceType> types = m.findAll(ClassOrInterfaceType.class);
                    for (ClassOrInterfaceType t : types) {
                        String tname = "";
                        try {
                            ResolvedReferenceType rt = t.resolve();
                            tname = rt.getQualifiedName();
                            nodesDf.append(i++, Arrays.asList(tname, rt.getTypeDeclaration().get().isInterface() ? "I" : "C", ""));
                        } catch (UnsupportedOperationException e) {
                            tname = t.getNameWithScope();
                            nodesDf.append(i++, Arrays.asList(tname, t.getMetaModel().getTypeName(), ""));
                        } catch (UnsolvedSymbolException e) {
                            System.out.println(e);
                            tname = t.getNameWithScope();
                            nodesDf.append(i++, Arrays.asList(tname, t.getMetaModel().getTypeName(), ""));
                        }
                        System.out.println("\t\t" + tname);
                    }
                    List<MethodCallExpr> calls = m.findAll(MethodCallExpr.class);
                    for (MethodCallExpr call : calls) {
                        ResolvedMethodDeclaration rm = call.resolve();
                        String rmname = rm.getQualifiedSignature();
                        nodesDf.append(i++, Arrays.asList(rmname, "M", ""));
                        System.out.println("\t\t" + rm.getQualifiedSignature());
                    }
                }
            }
        }



        nodesDf.writeCsv("tmp/nodes.csv");
        connectionsDf.writeCsv("tmp/connections.csv");
    }

    private static void build(MavenProj proj) {
        System.out.println("Building project " + proj.getPom().getAbsolutePath());
        boolean success = proj.cleanInstall();
        if (!success) {
            return;
        }
    }
}