package co.edu.javeriana.jreng.dep;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;

public class DepFinder {
    private Set<List<String>> deps = new HashSet<>();
    private Map<String, List<String>> nodes = new HashMap<>();
    private Catalog cat = new Catalog();
    private String excludedNodes = "";

    public DepFinder(String excludedNodes) {
        this.excludedNodes = excludedNodes;
    }

    protected void addNode(String id, NodeType t) {
        if (!id.matches(excludedNodes)) {
            nodes.put(id, new Node(id, t.getType()));
        }
    }

    protected void addDep(String src, String dst, DepType t) {
        if (!src.matches(excludedNodes) && !dst.matches(excludedNodes)) {
            deps.add(new Dependency(src, dst, t.getType()));
        }
    }

    public File visit(File javaFile) throws FileNotFoundException {
        CompilationUnit cu = StaticJavaParser.parse(javaFile);
        cu.findAll(ClassOrInterfaceDeclaration.class).forEach(c -> visit(c));
        return javaFile;
    }

    public String visit(ClassOrInterfaceDeclaration decl) {
        String cid = cat.idOf(decl);
        if (!nodes.containsKey(cid)) {
            addNode(cid, decl.isInterface() ? NodeType.INTERFACE : NodeType.CLASS);
            decl.findAll(MethodDeclaration.class).forEach(m -> addDep(cid, visit(m), DepType.HAS));
            decl.findAll(FieldDeclaration.class).forEach(f -> addDep(cid, visit(f), DepType.HAS));

        }
        return cid;
    }

    public String visit(MethodDeclaration method) {
        String mid = cat.idOf(method);
        addNode(mid, NodeType.METHOD);
        addDep(mid, visit(method.getType()), DepType.RETURN_TYPE);
        method.getParameters().forEach(p -> addDep(mid, visit(p.getType()), DepType.HAS));
        return mid;
    }

    private String visit(Type type) {
        return cat.idOf(type);
    }

    public String visit(FieldDeclaration field) {
        String mid = cat.idOf(field);
        addNode(mid, NodeType.FIELD);
        return mid;
    }

    public String visit(ConstructorDeclaration constr) {
        String mid = cat.idOf(constr);
        addNode(mid, NodeType.CONSTRUCTOR);
        constr.getParameters().forEach(p -> addDep(mid, visit(p.getType()), DepType.HAS));
        return mid;
    }

    public String visit(ClassOrInterfaceType t) {
        String tid = "";
        try {
            ResolvedReferenceType rt = t.resolve();
            tid = rt.getQualifiedName();
            Optional<ResolvedReferenceTypeDeclaration> c = rt.getTypeDeclaration();
            if (c.isPresent()) {
                addNode(tid, c.get().isInterface() ? NodeType.INTERFACE : NodeType.CLASS);
            }
        } catch (UnsupportedOperationException e) {
            tid = t.getNameWithScope();
        } catch (UnsolvedSymbolException e) {
            System.out.println(e);
            tid = t.getNameWithScope();
        }
        return tid;
    }

    public String visit(MethodCallExpr call) {
        ResolvedMethodDeclaration rmd = call.resolve();
        String rmname = rmd.getQualifiedSignature();
        addNode(rmname, NodeType.METHOD);
        return cat.idOf(call);
    }

    public Iterable<List<String>> getDeps() {
        return deps;
    }

    public Iterable<List<String>> getNodes() {
        return nodes.values();
    }

}
