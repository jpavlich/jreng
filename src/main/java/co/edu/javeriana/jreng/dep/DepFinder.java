package co.edu.javeriana.jreng.dep;

import java.util.HashSet;
import java.util.Set;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.type.Type;

public class DepFinder {
    private Set<Dependency> deps = new HashSet<>();
    private Catalog cat = new Catalog();

    protected void addDep(String src, String dst, String type) {
        deps.add(new Dependency(src, dst, type));
    }

    public void deps(TypeDeclaration<?> clazz) {
        String cid = cat.idOf(clazz);
        clazz.findAll(MethodDeclaration.class).forEach(m -> addDep(cid, cat.idOf(m), "m"));
    }

    public void deps(MethodDeclaration method) {
        String mid = cat.idOf(method);
        method.getParameters().forEach(p -> addDep(mid, cat.idOf(p.getType()), "p"));
    }
    public void deps(ConstructorDeclaration constr) {
        String mid = cat.idOf(constr);
        constr.getParameters().forEach(p -> addDep(mid, cat.idOf(p.getType()), "p"));
    }

    public void deps(Type type) {

    }

    public void deps(MethodCallExpr exp) {

    }


}
