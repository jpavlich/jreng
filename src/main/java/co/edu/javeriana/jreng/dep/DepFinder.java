package co.edu.javeriana.jreng.dep;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.AnnotationMemberDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.ReceiverParameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.InstanceOfExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.LongLiteralExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.PatternExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.expr.SuperExpr;
import com.github.javaparser.ast.expr.SwitchExpr;
import com.github.javaparser.ast.expr.TextBlockLiteralExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.expr.TypeExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.modules.ModuleExportsDirective;
import com.github.javaparser.ast.modules.ModuleOpensDirective;
import com.github.javaparser.ast.modules.ModuleProvidesDirective;
import com.github.javaparser.ast.modules.ModuleRequiresDirective;
import com.github.javaparser.ast.modules.ModuleUsesDirective;
import com.github.javaparser.ast.stmt.AssertStmt;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.BreakStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.ContinueStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.EmptyStmt;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.LabeledStmt;
import com.github.javaparser.ast.stmt.LocalClassDeclarationStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.SynchronizedStmt;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.stmt.UnparsableStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.stmt.YieldStmt;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.IntersectionType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.type.UnionType;
import com.github.javaparser.ast.type.UnknownType;
import com.github.javaparser.ast.type.VarType;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.type.WildcardType;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;

public class DepFinder implements GenericVisitor<String, Object> {

    private Catalog cat = new Catalog();
    private DepGraph depGraph;
    private String excludedNodes;

    public DepFinder(DepGraph depGraph, String excludedNodes) {
        this.depGraph = depGraph;
        this.excludedNodes = excludedNodes;

    }

    protected void addNode(String id, NodeType t) {
        if (!id.matches(excludedNodes)) {
            depGraph.addNode(id, cat.shortName(id), t);
        }
    }

    protected void addDep(String src, String dst, DepType t) {
        if (!src.matches(excludedNodes) && !dst.matches(excludedNodes)) {
            depGraph.addDep(src, dst, t.getType(), t);
        }
    }

    public File visit(File javaFile, Object arg) throws FileNotFoundException {
        CompilationUnit cu = StaticJavaParser.parse(javaFile);
        visit(cu, arg);
        return javaFile;
    }

    @Override
    public String visit(CompilationUnit cu, Object arg) {
        cu.findAll(ClassOrInterfaceDeclaration.class).forEach(c -> visit(c, arg));
        return cu.toString();
    }

    @Override
    public String visit(ClassOrInterfaceDeclaration decl, Object arg) {
        String id = cat.idOf(decl);
        if (!depGraph.hasNode(id)) {
            addNode(id, decl.isInterface() ? NodeType.INTERFACE : NodeType.CLASS);
            decl.getMethods().forEach(m -> addDep(id, visit(m, arg), DepType.HAS_METHOD));
            decl.getFields().forEach(f -> addDep(id, visit(f, arg), DepType.HAS_METHOD));
            decl.getAnnotations() .forEach(a -> addDep(id, visit(a, arg), DepType.HAS_ANNOTATION));
        }
        return id;
    }

    private String visit(AnnotationExpr a, Object arg) {
        String aid = cat.idOf(a);
        addNode(aid, NodeType.ANNOTATION);
        return aid;
    }

    @Override
    public String visit(MethodDeclaration method, Object arg) {
        String mid = cat.idOf(method);
        if (mid == null) return null;
        addNode(mid, NodeType.METHOD);

        addDep(mid, visit(method.getType(), arg), DepType.RETURN_TYPE);
        method.getParameters().forEach(p -> addDep(mid, visit(p.getType(), arg), DepType.HAS_PARAM));
        return mid;
    }

    protected String visit(Type type, Object arg) {
        if (type.isClassOrInterfaceType()) {
            return visit(type.asClassOrInterfaceType(), arg);
        } else {
            return cat.idOf(type);
        }
    }

    @Override
    public String visit(FieldDeclaration field, Object arg) {
        String mid = cat.idOf(field);
        addNode(mid, NodeType.FIELD);
        return mid;
    }

    @Override
    public String visit(ConstructorDeclaration constr, Object arg) {
        String mid = cat.idOf(constr);
        addNode(mid, NodeType.CONSTRUCTOR);
        constr.getParameters().forEach(p -> addDep(mid, visit(p.getType(), arg), DepType.HAS_PARAM));
        return mid;
    }

    @Override
    public String visit(ClassOrInterfaceType t, Object arg) {
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

    @Override
    public String visit(MethodCallExpr call, Object arg) {
        ResolvedMethodDeclaration rmd = call.resolve();
        String rmname = rmd.getQualifiedSignature();
        addNode(rmname, NodeType.METHOD);
        return cat.idOf(call);
    }

    @Override
    public String visit(PackageDeclaration n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(TypeParameter n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(LineComment n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(BlockComment n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(EnumDeclaration n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(EnumConstantDeclaration n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(AnnotationDeclaration n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(AnnotationMemberDeclaration n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(VariableDeclarator n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Parameter n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(InitializerDeclaration n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(JavadocComment n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(PrimitiveType n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(ArrayType n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(ArrayCreationLevel n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(IntersectionType n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(UnionType n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(VoidType n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(WildcardType n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(UnknownType n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(ArrayAccessExpr n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(ArrayCreationExpr n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(ArrayInitializerExpr n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(AssignExpr n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(BinaryExpr n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(CastExpr n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(ClassExpr n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(ConditionalExpr n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(EnclosedExpr n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(FieldAccessExpr n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(InstanceOfExpr n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(StringLiteralExpr n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(IntegerLiteralExpr n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(LongLiteralExpr n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(CharLiteralExpr n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(DoubleLiteralExpr n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(BooleanLiteralExpr n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(NullLiteralExpr n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(NameExpr n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(ObjectCreationExpr n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(ThisExpr n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(SuperExpr n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(UnaryExpr n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(VariableDeclarationExpr n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(MarkerAnnotationExpr n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(SingleMemberAnnotationExpr n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(NormalAnnotationExpr n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(MemberValuePair n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(ExplicitConstructorInvocationStmt n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(LocalClassDeclarationStmt n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(AssertStmt n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(BlockStmt n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(LabeledStmt n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(EmptyStmt n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(ExpressionStmt n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(SwitchStmt n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(SwitchEntry n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(BreakStmt n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(ReturnStmt n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(IfStmt n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(WhileStmt n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(ContinueStmt n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(DoStmt n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(ForEachStmt n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(ForStmt n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(ThrowStmt n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(SynchronizedStmt n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(TryStmt n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(CatchClause n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(LambdaExpr n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(MethodReferenceExpr n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(TypeExpr n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(NodeList n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Name n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(SimpleName n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(ImportDeclaration n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(ModuleDeclaration n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(ModuleRequiresDirective n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(ModuleExportsDirective n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(ModuleProvidesDirective n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(ModuleUsesDirective n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(ModuleOpensDirective n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(UnparsableStmt n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(ReceiverParameter n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(VarType n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Modifier n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(SwitchExpr n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(YieldStmt n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(TextBlockLiteralExpr n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(PatternExpr n, Object arg) {
        // TODO Auto-generated method stub
        return null;
    }

}
