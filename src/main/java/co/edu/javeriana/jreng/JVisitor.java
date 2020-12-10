package co.edu.javeriana.jreng;

import java.util.HashMap;
import java.util.Map;

import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
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
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
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
import com.github.javaparser.ast.nodeTypes.NodeWithName;
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
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.type.UnionType;
import com.github.javaparser.ast.type.UnknownType;
import com.github.javaparser.ast.type.VarType;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.type.WildcardType;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.Visitable;

public class JVisitor implements GenericVisitor<Visitable, Visitable> {

    @FunctionalInterface
    private interface VisitorFunction<V, A, S> {
        S apply(V v, A src);
    }

    private Map<Class<? extends Visitable>, VisitorFunction<? extends Visitable, ? extends Visitable, ? extends Visitable>> dispatcher = new HashMap<>();

    public JVisitor() {
        dispatcher.put(CompilationUnit.class, (CompilationUnit v, Visitable src) -> visit(v, src));
        dispatcher.put(PackageDeclaration.class, (PackageDeclaration v, Visitable src) -> visit(v, src));
        dispatcher.put(TypeParameter.class, (TypeParameter v, Visitable src) -> visit(v, src));
        dispatcher.put(LineComment.class, (LineComment v, Visitable src) -> visit(v, src));
        dispatcher.put(BlockComment.class, (BlockComment v, Visitable src) -> visit(v, src));
        dispatcher.put(ClassOrInterfaceDeclaration.class,
                (ClassOrInterfaceDeclaration v, Visitable src) -> visit(v, src));
        dispatcher.put(EnumDeclaration.class, (EnumDeclaration v, Visitable src) -> visit(v, src));
        dispatcher.put(EnumConstantDeclaration.class, (EnumConstantDeclaration v, Visitable src) -> visit(v, src));
        dispatcher.put(AnnotationDeclaration.class, (AnnotationDeclaration v, Visitable src) -> visit(v, src));
        dispatcher.put(AnnotationMemberDeclaration.class,
                (AnnotationMemberDeclaration v, Visitable src) -> visit(v, src));
        dispatcher.put(FieldDeclaration.class, (FieldDeclaration v, Visitable src) -> visit(v, src));
        dispatcher.put(VariableDeclarator.class, (VariableDeclarator v, Visitable src) -> visit(v, src));
        dispatcher.put(ConstructorDeclaration.class, (ConstructorDeclaration v, Visitable src) -> visit(v, src));
        dispatcher.put(MethodDeclaration.class, (MethodDeclaration v, Visitable src) -> visit(v, src));
        dispatcher.put(Parameter.class, (Parameter v, Visitable src) -> visit(v, src));
        dispatcher.put(InitializerDeclaration.class, (InitializerDeclaration v, Visitable src) -> visit(v, src));
        dispatcher.put(JavadocComment.class, (JavadocComment v, Visitable src) -> visit(v, src));
        dispatcher.put(ClassOrInterfaceType.class, (ClassOrInterfaceType v, Visitable src) -> visit(v, src));
        dispatcher.put(PrimitiveType.class, (PrimitiveType v, Visitable src) -> visit(v, src));
        dispatcher.put(ArrayType.class, (ArrayType v, Visitable src) -> visit(v, src));
        dispatcher.put(ArrayCreationLevel.class, (ArrayCreationLevel v, Visitable src) -> visit(v, src));
        dispatcher.put(IntersectionType.class, (IntersectionType v, Visitable src) -> visit(v, src));
        dispatcher.put(UnionType.class, (UnionType v, Visitable src) -> visit(v, src));
        dispatcher.put(VoidType.class, (VoidType v, Visitable src) -> visit(v, src));
        dispatcher.put(WildcardType.class, (WildcardType v, Visitable src) -> visit(v, src));
        dispatcher.put(UnknownType.class, (UnknownType v, Visitable src) -> visit(v, src));
        dispatcher.put(ArrayAccessExpr.class, (ArrayAccessExpr v, Visitable src) -> visit(v, src));
        dispatcher.put(ArrayCreationExpr.class, (ArrayCreationExpr v, Visitable src) -> visit(v, src));
        dispatcher.put(ArrayInitializerExpr.class, (ArrayInitializerExpr v, Visitable src) -> visit(v, src));
        dispatcher.put(AssignExpr.class, (AssignExpr v, Visitable src) -> visit(v, src));
        dispatcher.put(BinaryExpr.class, (BinaryExpr v, Visitable src) -> visit(v, src));
        dispatcher.put(CastExpr.class, (CastExpr v, Visitable src) -> visit(v, src));
        dispatcher.put(ClassExpr.class, (ClassExpr v, Visitable src) -> visit(v, src));
        dispatcher.put(ConditionalExpr.class, (ConditionalExpr v, Visitable src) -> visit(v, src));
        dispatcher.put(EnclosedExpr.class, (EnclosedExpr v, Visitable src) -> visit(v, src));
        dispatcher.put(FieldAccessExpr.class, (FieldAccessExpr v, Visitable src) -> visit(v, src));
        dispatcher.put(InstanceOfExpr.class, (InstanceOfExpr v, Visitable src) -> visit(v, src));
        dispatcher.put(StringLiteralExpr.class, (StringLiteralExpr v, Visitable src) -> visit(v, src));
        dispatcher.put(IntegerLiteralExpr.class, (IntegerLiteralExpr v, Visitable src) -> visit(v, src));
        dispatcher.put(LongLiteralExpr.class, (LongLiteralExpr v, Visitable src) -> visit(v, src));
        dispatcher.put(CharLiteralExpr.class, (CharLiteralExpr v, Visitable src) -> visit(v, src));
        dispatcher.put(DoubleLiteralExpr.class, (DoubleLiteralExpr v, Visitable src) -> visit(v, src));
        dispatcher.put(BooleanLiteralExpr.class, (BooleanLiteralExpr v, Visitable src) -> visit(v, src));
        dispatcher.put(NullLiteralExpr.class, (NullLiteralExpr v, Visitable src) -> visit(v, src));
        dispatcher.put(MethodCallExpr.class, (MethodCallExpr v, Visitable src) -> visit(v, src));
        dispatcher.put(NameExpr.class, (NameExpr v, Visitable src) -> visit(v, src));
        dispatcher.put(ObjectCreationExpr.class, (ObjectCreationExpr v, Visitable src) -> visit(v, src));
        dispatcher.put(ThisExpr.class, (ThisExpr v, Visitable src) -> visit(v, src));
        dispatcher.put(SuperExpr.class, (SuperExpr v, Visitable src) -> visit(v, src));
        dispatcher.put(UnaryExpr.class, (UnaryExpr v, Visitable src) -> visit(v, src));
        dispatcher.put(VariableDeclarationExpr.class, (VariableDeclarationExpr v, Visitable src) -> visit(v, src));
        dispatcher.put(MarkerAnnotationExpr.class, (MarkerAnnotationExpr v, Visitable src) -> visit(v, src));
        dispatcher.put(SingleMemberAnnotationExpr.class,
                (SingleMemberAnnotationExpr v, Visitable src) -> visit(v, src));
        dispatcher.put(NormalAnnotationExpr.class, (NormalAnnotationExpr v, Visitable src) -> visit(v, src));
        dispatcher.put(MemberValuePair.class, (MemberValuePair v, Visitable src) -> visit(v, src));
        dispatcher.put(ExplicitConstructorInvocationStmt.class,
                (ExplicitConstructorInvocationStmt v, Visitable src) -> visit(v, src));
        dispatcher.put(LocalClassDeclarationStmt.class, (LocalClassDeclarationStmt v, Visitable src) -> visit(v, src));
        dispatcher.put(AssertStmt.class, (AssertStmt v, Visitable src) -> visit(v, src));
        dispatcher.put(BlockStmt.class, (BlockStmt v, Visitable src) -> visit(v, src));
        dispatcher.put(LabeledStmt.class, (LabeledStmt v, Visitable src) -> visit(v, src));
        dispatcher.put(EmptyStmt.class, (EmptyStmt v, Visitable src) -> visit(v, src));
        dispatcher.put(ExpressionStmt.class, (ExpressionStmt v, Visitable src) -> visit(v, src));
        dispatcher.put(SwitchStmt.class, (SwitchStmt v, Visitable src) -> visit(v, src));
        dispatcher.put(SwitchEntry.class, (SwitchEntry v, Visitable src) -> visit(v, src));
        dispatcher.put(BreakStmt.class, (BreakStmt v, Visitable src) -> visit(v, src));
        dispatcher.put(ReturnStmt.class, (ReturnStmt v, Visitable src) -> visit(v, src));
        dispatcher.put(IfStmt.class, (IfStmt v, Visitable src) -> visit(v, src));
        dispatcher.put(WhileStmt.class, (WhileStmt v, Visitable src) -> visit(v, src));
        dispatcher.put(ContinueStmt.class, (ContinueStmt v, Visitable src) -> visit(v, src));
        dispatcher.put(DoStmt.class, (DoStmt v, Visitable src) -> visit(v, src));
        dispatcher.put(ForEachStmt.class, (ForEachStmt v, Visitable src) -> visit(v, src));
        dispatcher.put(ForStmt.class, (ForStmt v, Visitable src) -> visit(v, src));
        dispatcher.put(ThrowStmt.class, (ThrowStmt v, Visitable src) -> visit(v, src));
        dispatcher.put(SynchronizedStmt.class, (SynchronizedStmt v, Visitable src) -> visit(v, src));
        dispatcher.put(TryStmt.class, (TryStmt v, Visitable src) -> visit(v, src));
        dispatcher.put(CatchClause.class, (CatchClause v, Visitable src) -> visit(v, src));
        dispatcher.put(LambdaExpr.class, (LambdaExpr v, Visitable src) -> visit(v, src));
        dispatcher.put(MethodReferenceExpr.class, (MethodReferenceExpr v, Visitable src) -> visit(v, src));
        dispatcher.put(TypeExpr.class, (TypeExpr v, Visitable src) -> visit(v, src));
        dispatcher.put(NodeList.class, (NodeList v, Visitable src) -> visit(v, src));
        dispatcher.put(Name.class, (Name v, Visitable src) -> visit(v, src));
        dispatcher.put(SimpleName.class, (SimpleName v, Visitable src) -> visit(v, src));
        dispatcher.put(ImportDeclaration.class, (ImportDeclaration v, Visitable src) -> visit(v, src));
        dispatcher.put(ModuleDeclaration.class, (ModuleDeclaration v, Visitable src) -> visit(v, src));
        dispatcher.put(ModuleRequiresDirective.class, (ModuleRequiresDirective v, Visitable src) -> visit(v, src));
        dispatcher.put(ModuleExportsDirective.class, (ModuleExportsDirective v, Visitable src) -> visit(v, src));
        dispatcher.put(ModuleProvidesDirective.class, (ModuleProvidesDirective v, Visitable src) -> visit(v, src));
        dispatcher.put(ModuleUsesDirective.class, (ModuleUsesDirective v, Visitable src) -> visit(v, src));
        dispatcher.put(ModuleOpensDirective.class, (ModuleOpensDirective v, Visitable src) -> visit(v, src));
        dispatcher.put(UnparsableStmt.class, (UnparsableStmt v, Visitable src) -> visit(v, src));
        dispatcher.put(ReceiverParameter.class, (ReceiverParameter v, Visitable src) -> visit(v, src));
        dispatcher.put(VarType.class, (VarType v, Visitable src) -> visit(v, src));
        dispatcher.put(Modifier.class, (Modifier v, Visitable src) -> visit(v, src));
        dispatcher.put(SwitchExpr.class, (SwitchExpr v, Visitable src) -> visit(v, src));
        dispatcher.put(YieldStmt.class, (YieldStmt v, Visitable src) -> visit(v, src));
        dispatcher.put(TextBlockLiteralExpr.class, (TextBlockLiteralExpr v, Visitable src) -> visit(v, src));
    }

    public Node dispatch(Node n, Visitable src) {
        VisitorFunction<Node, Visitable, Node> f = (VisitorFunction<Node, Visitable, Node>) dispatcher
                .get(n.getClass());
        Node node = f.apply(n, src);
        visitChildren(n, src);
        return node;
    }

    protected Visitable visitChildren(Node n, Visitable src) {
        for (Node ch : n.getChildNodes()) {
            dispatch(ch, src);
        }
        return n;
    }

    protected String getName(Visitable v) {
        if (v == null) {
            return "";
        }
        String name = "";
        if (v instanceof TypeDeclaration) {
            name += ((TypeDeclaration<?>) v).resolve().getQualifiedName();
        }
        if (v instanceof MethodDeclaration) {
            name += ((MethodDeclaration) v).resolve().getQualifiedName();
        }
        if (v instanceof NodeWithName) {
            name += ((NodeWithName<?>) v).getName().getIdentifier();
        }


        name += ":" + v.getClass().getSimpleName();
        return name;

    }

    protected void print(Visitable v, Visitable src) {
        if (v instanceof Node) {
            Node n = (Node) v;
            while (n.getParentNode().isPresent()) {
                System.out.print("\t");
                n = n.getParentNode().get();
            }
        }
        System.out.println(getName(v));
        // System.out.println(getName(src) + " -> " + getName(v));

    }

    @Override
    public Visitable visit(CompilationUnit n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(PackageDeclaration n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(TypeParameter n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(LineComment n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(BlockComment n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(ClassOrInterfaceDeclaration n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(EnumDeclaration n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(EnumConstantDeclaration n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(AnnotationDeclaration n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(AnnotationMemberDeclaration n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(FieldDeclaration n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(VariableDeclarator n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(ConstructorDeclaration n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(MethodDeclaration n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(Parameter n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(InitializerDeclaration n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(JavadocComment n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(ClassOrInterfaceType n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(PrimitiveType n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(ArrayType n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(ArrayCreationLevel n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(IntersectionType n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(UnionType n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(VoidType n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(WildcardType n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(UnknownType n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(ArrayAccessExpr n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(ArrayCreationExpr n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(ArrayInitializerExpr n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(AssignExpr n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(BinaryExpr n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(CastExpr n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(ClassExpr n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(ConditionalExpr n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(EnclosedExpr n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(FieldAccessExpr n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(InstanceOfExpr n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(StringLiteralExpr n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(IntegerLiteralExpr n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(LongLiteralExpr n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(CharLiteralExpr n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(DoubleLiteralExpr n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(BooleanLiteralExpr n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(NullLiteralExpr n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(MethodCallExpr n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(NameExpr n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(ObjectCreationExpr n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(ThisExpr n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(SuperExpr n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(UnaryExpr n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(VariableDeclarationExpr n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(MarkerAnnotationExpr n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(SingleMemberAnnotationExpr n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(NormalAnnotationExpr n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(MemberValuePair n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(ExplicitConstructorInvocationStmt n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(LocalClassDeclarationStmt n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(AssertStmt n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(BlockStmt n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(LabeledStmt n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(EmptyStmt n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(ExpressionStmt n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(SwitchStmt n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(SwitchEntry n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(BreakStmt n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(ReturnStmt n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(IfStmt n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(WhileStmt n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(ContinueStmt n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(DoStmt n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(ForEachStmt n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(ForStmt n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(ThrowStmt n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(SynchronizedStmt n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(TryStmt n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(CatchClause n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(LambdaExpr n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(MethodReferenceExpr n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(TypeExpr n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(NodeList n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(Name n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(SimpleName n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(ImportDeclaration n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(ModuleDeclaration n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(ModuleRequiresDirective n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(ModuleExportsDirective n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(ModuleProvidesDirective n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(ModuleUsesDirective n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(ModuleOpensDirective n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(UnparsableStmt n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(ReceiverParameter n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(VarType n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(Modifier n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(SwitchExpr n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(YieldStmt n, Visitable src) {
        print(n, src);
        return n;
    }

    @Override
    public Visitable visit(TextBlockLiteralExpr n, Visitable src) {
        print(n, src);
        return n;
    }

}
