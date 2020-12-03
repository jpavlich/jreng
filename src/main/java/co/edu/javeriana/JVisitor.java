package co.edu.javeriana;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.types.ResolvedReferenceType;

public class JVisitor {
    String visit(CompilationUnit node) {
        // node.findAll(ClassOrInterfaceDeclaration.class).forEach(c -> visit(c));
        node.getTypes().forEach(t -> visit(t));
        return node.toString();
    }

    String visit(TypeDeclaration<?> t) {
        t.getFields().forEach(f -> visit(f));
        t.getMethods().forEach(m -> visit(m));
        
        return t.resolve().getQualifiedName();
        // if (t.isClassOrInterfaceDeclaration()) {
        // return visit((ClassOrInterfaceDeclaration) t);
        // } else if (t.isAnnotationDeclaration()) {
        // return visit((AnnotationDeclaration) t);
        // } else {
        // return visit((EnumDeclaration) t);
        // }
    }

    // String visit(ClassOrInterfaceDeclaration node) {
    // node.getMethods().forEach(m -> visit(m));
    // return node.resolve().getQualifiedName();
    // }

    // String visit(AnnotationDeclaration node) {
    // node.findAll(ClassOrInterfaceType.class).forEach(c -> visit(c));
    // return node.resolve().getQualifiedName();
    // }

    // String visit(EnumDeclaration node) {
    // node.getMethods().forEach(m -> visit(m));
    // return node.resolve().getQualifiedName();
    // }

    private String visit(FieldDeclaration f) {
        return f.resolve().getName();
    }

    String visit(MethodDeclaration node) {
        node.findAll(ClassOrInterfaceType.class).forEach(c -> visit(c));
        node.findAll(MethodCallExpr.class).forEach(m -> visit(m));
        return node.resolve().getQualifiedSignature();
    }

    String visit(ClassOrInterfaceType node) {
        try {
            ResolvedReferenceType rt = node.resolve();
            return rt.getQualifiedName();
        } catch (UnsupportedOperationException e) {
            return node.getNameWithScope();
        } catch (UnsolvedSymbolException e) {
            System.out.println(e);
            return node.getNameWithScope();
        }
    }

    String visit(MethodCallExpr node) {
        node.getArguments().forEach(a -> visit(a));
        node.getTypeArguments().orElse(new NodeList<>()).forEach(a -> visit(a));
        return node.resolve().getQualifiedSignature();
    }

    String visit(Type a) {
        return null;
    }

    String visit(Expression a) {
        return null;
    }

}
