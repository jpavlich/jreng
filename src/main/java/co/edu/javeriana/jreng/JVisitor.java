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

public class JVisitor implements GenericVisitor<Visitable, Integer> {

    @FunctionalInterface
    private interface VisitorFunction<V, A, S> {
        S apply(V v, A lvl);
    }

    private Map<Class<? extends Visitable>, VisitorFunction<? extends Visitable, ? extends Integer, ? extends Visitable>> dispatcher = new HashMap<>();

    public JVisitor() {
        dispatcher.put(CompilationUnit.class, (CompilationUnit v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(PackageDeclaration.class, (PackageDeclaration v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(TypeParameter.class, (TypeParameter v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(LineComment.class, (LineComment v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(BlockComment.class, (BlockComment v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(ClassOrInterfaceDeclaration.class,
                (ClassOrInterfaceDeclaration v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(EnumDeclaration.class, (EnumDeclaration v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(EnumConstantDeclaration.class, (EnumConstantDeclaration v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(AnnotationDeclaration.class, (AnnotationDeclaration v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(AnnotationMemberDeclaration.class,
                (AnnotationMemberDeclaration v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(FieldDeclaration.class, (FieldDeclaration v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(VariableDeclarator.class, (VariableDeclarator v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(ConstructorDeclaration.class, (ConstructorDeclaration v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(MethodDeclaration.class, (MethodDeclaration v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(Parameter.class, (Parameter v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(InitializerDeclaration.class, (InitializerDeclaration v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(JavadocComment.class, (JavadocComment v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(ClassOrInterfaceType.class, (ClassOrInterfaceType v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(PrimitiveType.class, (PrimitiveType v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(ArrayType.class, (ArrayType v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(ArrayCreationLevel.class, (ArrayCreationLevel v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(IntersectionType.class, (IntersectionType v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(UnionType.class, (UnionType v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(VoidType.class, (VoidType v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(WildcardType.class, (WildcardType v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(UnknownType.class, (UnknownType v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(ArrayAccessExpr.class, (ArrayAccessExpr v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(ArrayCreationExpr.class, (ArrayCreationExpr v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(ArrayInitializerExpr.class, (ArrayInitializerExpr v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(AssignExpr.class, (AssignExpr v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(BinaryExpr.class, (BinaryExpr v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(CastExpr.class, (CastExpr v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(ClassExpr.class, (ClassExpr v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(ConditionalExpr.class, (ConditionalExpr v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(EnclosedExpr.class, (EnclosedExpr v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(FieldAccessExpr.class, (FieldAccessExpr v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(InstanceOfExpr.class, (InstanceOfExpr v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(StringLiteralExpr.class, (StringLiteralExpr v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(IntegerLiteralExpr.class, (IntegerLiteralExpr v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(LongLiteralExpr.class, (LongLiteralExpr v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(CharLiteralExpr.class, (CharLiteralExpr v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(DoubleLiteralExpr.class, (DoubleLiteralExpr v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(BooleanLiteralExpr.class, (BooleanLiteralExpr v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(NullLiteralExpr.class, (NullLiteralExpr v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(MethodCallExpr.class, (MethodCallExpr v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(NameExpr.class, (NameExpr v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(ObjectCreationExpr.class, (ObjectCreationExpr v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(ThisExpr.class, (ThisExpr v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(SuperExpr.class, (SuperExpr v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(UnaryExpr.class, (UnaryExpr v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(VariableDeclarationExpr.class, (VariableDeclarationExpr v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(MarkerAnnotationExpr.class, (MarkerAnnotationExpr v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(SingleMemberAnnotationExpr.class, (SingleMemberAnnotationExpr v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(NormalAnnotationExpr.class, (NormalAnnotationExpr v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(MemberValuePair.class, (MemberValuePair v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(ExplicitConstructorInvocationStmt.class,
                (ExplicitConstructorInvocationStmt v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(LocalClassDeclarationStmt.class, (LocalClassDeclarationStmt v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(AssertStmt.class, (AssertStmt v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(BlockStmt.class, (BlockStmt v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(LabeledStmt.class, (LabeledStmt v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(EmptyStmt.class, (EmptyStmt v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(ExpressionStmt.class, (ExpressionStmt v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(SwitchStmt.class, (SwitchStmt v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(SwitchEntry.class, (SwitchEntry v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(BreakStmt.class, (BreakStmt v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(ReturnStmt.class, (ReturnStmt v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(IfStmt.class, (IfStmt v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(WhileStmt.class, (WhileStmt v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(ContinueStmt.class, (ContinueStmt v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(DoStmt.class, (DoStmt v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(ForEachStmt.class, (ForEachStmt v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(ForStmt.class, (ForStmt v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(ThrowStmt.class, (ThrowStmt v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(SynchronizedStmt.class, (SynchronizedStmt v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(TryStmt.class, (TryStmt v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(CatchClause.class, (CatchClause v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(LambdaExpr.class, (LambdaExpr v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(MethodReferenceExpr.class, (MethodReferenceExpr v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(TypeExpr.class, (TypeExpr v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(NodeList.class, (NodeList v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(Name.class, (Name v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(SimpleName.class, (SimpleName v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(ImportDeclaration.class, (ImportDeclaration v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(ModuleDeclaration.class, (ModuleDeclaration v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(ModuleRequiresDirective.class, (ModuleRequiresDirective v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(ModuleExportsDirective.class, (ModuleExportsDirective v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(ModuleProvidesDirective.class, (ModuleProvidesDirective v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(ModuleUsesDirective.class, (ModuleUsesDirective v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(ModuleOpensDirective.class, (ModuleOpensDirective v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(UnparsableStmt.class, (UnparsableStmt v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(ReceiverParameter.class, (ReceiverParameter v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(VarType.class, (VarType v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(Modifier.class, (Modifier v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(SwitchExpr.class, (SwitchExpr v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(YieldStmt.class, (YieldStmt v, Integer lvl) -> visit(v, lvl));
        dispatcher.put(TextBlockLiteralExpr.class, (TextBlockLiteralExpr v, Integer lvl) -> visit(v, lvl));
    }

    public Node dispatch(Node n, Integer level) {
        VisitorFunction<Node, Integer, Node> f = (VisitorFunction<Node, Integer, Node>) dispatcher.get(n.getClass());
        Node node = f.apply(n, level);
        visitChildren(n, level);
        return node;
    }

    protected void visitChildren(Node n, Integer level) {
        for (Node ch : n.getChildNodes()) {
            dispatch(ch, level + 1);
        }
    }

    protected void print(Visitable n, Integer level) {
        for (int i = 0; i < level; i++) {
            System.out.print("\t");
        }
        System.out.print(n.getClass().getSimpleName());
        if (n instanceof NodeWithName) {
            System.out.println(": " + ((NodeWithName<?>)n).getName().getIdentifier());
        } else {
            System.out.println();
        }
    }

    @Override
    public Visitable visit(CompilationUnit n, Integer level) {
        print(n, level);
        return n;
    } 

    @Override
    public Visitable visit(PackageDeclaration n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(TypeParameter n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(LineComment n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(BlockComment n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(ClassOrInterfaceDeclaration n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(EnumDeclaration n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(EnumConstantDeclaration n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(AnnotationDeclaration n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(AnnotationMemberDeclaration n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(FieldDeclaration n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(VariableDeclarator n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(ConstructorDeclaration n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(MethodDeclaration n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(Parameter n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(InitializerDeclaration n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(JavadocComment n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(ClassOrInterfaceType n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(PrimitiveType n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(ArrayType n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(ArrayCreationLevel n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(IntersectionType n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(UnionType n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(VoidType n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(WildcardType n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(UnknownType n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(ArrayAccessExpr n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(ArrayCreationExpr n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(ArrayInitializerExpr n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(AssignExpr n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(BinaryExpr n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(CastExpr n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(ClassExpr n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(ConditionalExpr n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(EnclosedExpr n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(FieldAccessExpr n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(InstanceOfExpr n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(StringLiteralExpr n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(IntegerLiteralExpr n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(LongLiteralExpr n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(CharLiteralExpr n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(DoubleLiteralExpr n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(BooleanLiteralExpr n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(NullLiteralExpr n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(MethodCallExpr n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(NameExpr n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(ObjectCreationExpr n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(ThisExpr n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(SuperExpr n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(UnaryExpr n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(VariableDeclarationExpr n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(MarkerAnnotationExpr n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(SingleMemberAnnotationExpr n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(NormalAnnotationExpr n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(MemberValuePair n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(ExplicitConstructorInvocationStmt n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(LocalClassDeclarationStmt n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(AssertStmt n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(BlockStmt n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(LabeledStmt n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(EmptyStmt n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(ExpressionStmt n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(SwitchStmt n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(SwitchEntry n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(BreakStmt n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(ReturnStmt n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(IfStmt n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(WhileStmt n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(ContinueStmt n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(DoStmt n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(ForEachStmt n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(ForStmt n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(ThrowStmt n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(SynchronizedStmt n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(TryStmt n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(CatchClause n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(LambdaExpr n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(MethodReferenceExpr n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(TypeExpr n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(NodeList n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(Name n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(SimpleName n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(ImportDeclaration n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(ModuleDeclaration n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(ModuleRequiresDirective n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(ModuleExportsDirective n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(ModuleProvidesDirective n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(ModuleUsesDirective n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(ModuleOpensDirective n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(UnparsableStmt n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(ReceiverParameter n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(VarType n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(Modifier n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(SwitchExpr n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(YieldStmt n, Integer level) {
        print(n, level);
        return n;
    }

    @Override
    public Visitable visit(TextBlockLiteralExpr n, Integer level) {
        print(n, level);
        return n;
    }

}
