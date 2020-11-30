package co.edu.javeriana;

import java.io.File;
import java.io.IOException;
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
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import org.apache.commons.lang3.tuple.ImmutablePair;

import javassist.compiler.ast.MethodDecl;

/**
 * Some code that uses JavaSymbolSolver.
 */
public class JReng {

    public static void main(String[] args) throws IOException {
        // Set up a minimal type solver that only looks at the classes used to run this
        // sample.
        CombinedTypeSolver typeSolver = new CombinedTypeSolver();
        typeSolver.add(new ReflectionTypeSolver());
        MavenProj proj = new MavenProj(new File("pom.xml"));
        boolean success = proj.cleanInstall();
        if (!success) {
            return;
        }

        List<File> jars = proj.depJars();
        for (File jarFile : jars) {
            typeSolver.add(new JarTypeSolver(jarFile));
        }

        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);
        Collection<File> javas = proj.javas();
        
        Set<ImmutablePair<String, String> > tuples = new HashSet<>();

        for (File javaFile : javas) {
            CompilationUnit cu = StaticJavaParser.parse(javaFile);
            List<ClassOrInterfaceDeclaration> classes = cu.findAll(ClassOrInterfaceDeclaration.class);
            for (ClassOrInterfaceDeclaration c : classes) {
                System.out.println(c.resolve().getQualifiedName());
                List<MethodDeclaration> methods = c.findAll(MethodDeclaration.class);
                for (MethodDeclaration m : methods) {
                    System.out.println("\t" + m.resolve().getQualifiedSignature());
                    List<ClassOrInterfaceType> types = m.findAll(ClassOrInterfaceType.class);
                    for (ClassOrInterfaceType t : types) {
                        System.out.println("\t\t" +  t.resolve().getQualifiedName());
                    }
                    List<MethodCallExpr> calls = m.findAll(MethodCallExpr.class);
                    for (MethodCallExpr call : calls) {
                        System.out.println("\t\t" +  call.resolve().getQualifiedSignature());
                    }
                }
            }
        }

        Csv csv = new Csv();
        csv.setColumns("src", "dst");
        for (ImmutablePair<String,String> pair : tuples) {
            csv.addRow(pair.left, pair.right);
        }

        csv.save(new File("tmp/out.csv"));


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
}