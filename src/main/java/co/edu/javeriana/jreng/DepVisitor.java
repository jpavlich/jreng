package co.edu.javeriana.jreng;

import java.util.HashSet;
import java.util.Set;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.Visitable;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;

public class DepVisitor extends JVisitor {

    Set<Dependency> deps = new HashSet<>();

    private void addDep(Node src, Node dst, String type) {
        deps.add(new Dependency(getName(src), getName(dst), type));
    }

    @Override
    public Visitable visit(ClassOrInterfaceDeclaration n, Visitable src) {
        super.visit(n, src);
        return super.visitChildren(n, n);
    }

    @Override
    public Visitable visit(ClassOrInterfaceType n, Visitable src) {
        super.visit(n, src);
        ResolvedReferenceTypeDeclaration t = n.resolve().getTypeDeclaration().get();
        
        return super.visitChildren(n, n);
    }
    
    @Override
    public Visitable visit(MethodDeclaration n, Visitable src) {
        super.visit(n,src);
        return super.visitChildren(n, n);
    }
    
    @Override
    public Visitable visit(MethodCallExpr n, Visitable src) {
        super.visit(n,src);
        return super.visitChildren(n, n);
    }
    
    @Override
    public Visitable visit(FieldDeclaration n, Visitable src) {
        super.visit(n,src);
        return super.visitChildren(n, n);
    }

}
