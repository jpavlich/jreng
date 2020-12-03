package co.edu.javeriana;

import java.util.HashMap;
import java.util.Map;

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

public class JVisitor implements GenericVisitor<Visitable, Object> {

    @FunctionalInterface
    private interface VisitorFunction<V, A, S> {
        S apply(V v, A a);
    }

    private Map<Class<? extends Visitable>, VisitorFunction<? extends Visitable, ? extends Object, ? extends Visitable>> dispatcher = new HashMap<>();

    public JVisitor() {
        dispatcher.put(CompilationUnit.class, (CompilationUnit v, Object a) -> visit(v, a));
        dispatcher.put(PackageDeclaration.class, (PackageDeclaration v, Object a) -> visit(v, a));
        dispatcher.put(TypeParameter.class, (TypeParameter v, Object a) -> visit(v, a));
        dispatcher.put(LineComment.class, (LineComment v, Object a) -> visit(v, a));
        dispatcher.put(BlockComment.class, (BlockComment v, Object a) -> visit(v, a));
        dispatcher.put(ClassOrInterfaceDeclaration.class, (ClassOrInterfaceDeclaration v, Object a) -> visit(v, a));
        dispatcher.put(EnumDeclaration.class, (EnumDeclaration v, Object a) -> visit(v, a));
        dispatcher.put(EnumConstantDeclaration.class, (EnumConstantDeclaration v, Object a) -> visit(v, a));
        dispatcher.put(AnnotationDeclaration.class, (AnnotationDeclaration v, Object a) -> visit(v, a));
        dispatcher.put(AnnotationMemberDeclaration.class, (AnnotationMemberDeclaration v, Object a) -> visit(v, a));
        dispatcher.put(FieldDeclaration.class, (FieldDeclaration v, Object a) -> visit(v, a));
        dispatcher.put(VariableDeclarator.class, (VariableDeclarator v, Object a) -> visit(v, a));
        dispatcher.put(ConstructorDeclaration.class, (ConstructorDeclaration v, Object a) -> visit(v, a));
        dispatcher.put(MethodDeclaration.class, (MethodDeclaration v, Object a) -> visit(v, a));
        dispatcher.put(Parameter.class, (Parameter v, Object a) -> visit(v, a));
        dispatcher.put(InitializerDeclaration.class, (InitializerDeclaration v, Object a) -> visit(v, a));
        dispatcher.put(JavadocComment.class, (JavadocComment v, Object a) -> visit(v, a));
        dispatcher.put(ClassOrInterfaceType.class, (ClassOrInterfaceType v, Object a) -> visit(v, a));
        dispatcher.put(PrimitiveType.class, (PrimitiveType v, Object a) -> visit(v, a));
        dispatcher.put(ArrayType.class, (ArrayType v, Object a) -> visit(v, a));
        dispatcher.put(ArrayCreationLevel.class, (ArrayCreationLevel v, Object a) -> visit(v, a));
        dispatcher.put(IntersectionType.class, (IntersectionType v, Object a) -> visit(v, a));
        dispatcher.put(UnionType.class, (UnionType v, Object a) -> visit(v, a));
        dispatcher.put(VoidType.class, (VoidType v, Object a) -> visit(v, a));
        dispatcher.put(WildcardType.class, (WildcardType v, Object a) -> visit(v, a));
        dispatcher.put(UnknownType.class, (UnknownType v, Object a) -> visit(v, a));
        dispatcher.put(ArrayAccessExpr.class, (ArrayAccessExpr v, Object a) -> visit(v, a));
        dispatcher.put(ArrayCreationExpr.class, (ArrayCreationExpr v, Object a) -> visit(v, a));
        dispatcher.put(ArrayInitializerExpr.class, (ArrayInitializerExpr v, Object a) -> visit(v, a));
        dispatcher.put(AssignExpr.class, (AssignExpr v, Object a) -> visit(v, a));
        dispatcher.put(BinaryExpr.class, (BinaryExpr v, Object a) -> visit(v, a));
        dispatcher.put(CastExpr.class, (CastExpr v, Object a) -> visit(v, a));
        dispatcher.put(ClassExpr.class, (ClassExpr v, Object a) -> visit(v, a));
        dispatcher.put(ConditionalExpr.class, (ConditionalExpr v, Object a) -> visit(v, a));
        dispatcher.put(EnclosedExpr.class, (EnclosedExpr v, Object a) -> visit(v, a));
        dispatcher.put(FieldAccessExpr.class, (FieldAccessExpr v, Object a) -> visit(v, a));
        dispatcher.put(InstanceOfExpr.class, (InstanceOfExpr v, Object a) -> visit(v, a));
        dispatcher.put(StringLiteralExpr.class, (StringLiteralExpr v, Object a) -> visit(v, a));
        dispatcher.put(IntegerLiteralExpr.class, (IntegerLiteralExpr v, Object a) -> visit(v, a));
        dispatcher.put(LongLiteralExpr.class, (LongLiteralExpr v, Object a) -> visit(v, a));
        dispatcher.put(CharLiteralExpr.class, (CharLiteralExpr v, Object a) -> visit(v, a));
        dispatcher.put(DoubleLiteralExpr.class, (DoubleLiteralExpr v, Object a) -> visit(v, a));
        dispatcher.put(BooleanLiteralExpr.class, (BooleanLiteralExpr v, Object a) -> visit(v, a));
        dispatcher.put(NullLiteralExpr.class, (NullLiteralExpr v, Object a) -> visit(v, a));
        dispatcher.put(MethodCallExpr.class, (MethodCallExpr v, Object a) -> visit(v, a));
        dispatcher.put(NameExpr.class, (NameExpr v, Object a) -> visit(v, a));
        dispatcher.put(ObjectCreationExpr.class, (ObjectCreationExpr v, Object a) -> visit(v, a));
        dispatcher.put(ThisExpr.class, (ThisExpr v, Object a) -> visit(v, a));
        dispatcher.put(SuperExpr.class, (SuperExpr v, Object a) -> visit(v, a));
        dispatcher.put(UnaryExpr.class, (UnaryExpr v, Object a) -> visit(v, a));
        dispatcher.put(VariableDeclarationExpr.class, (VariableDeclarationExpr v, Object a) -> visit(v, a));
        dispatcher.put(MarkerAnnotationExpr.class, (MarkerAnnotationExpr v, Object a) -> visit(v, a));
        dispatcher.put(SingleMemberAnnotationExpr.class, (SingleMemberAnnotationExpr v, Object a) -> visit(v, a));
        dispatcher.put(NormalAnnotationExpr.class, (NormalAnnotationExpr v, Object a) -> visit(v, a));
        dispatcher.put(MemberValuePair.class, (MemberValuePair v, Object a) -> visit(v, a));
        dispatcher.put(ExplicitConstructorInvocationStmt.class,
                (ExplicitConstructorInvocationStmt v, Object a) -> visit(v, a));
        dispatcher.put(LocalClassDeclarationStmt.class, (LocalClassDeclarationStmt v, Object a) -> visit(v, a));
        dispatcher.put(AssertStmt.class, (AssertStmt v, Object a) -> visit(v, a));
        dispatcher.put(BlockStmt.class, (BlockStmt v, Object a) -> visit(v, a));
        dispatcher.put(LabeledStmt.class, (LabeledStmt v, Object a) -> visit(v, a));
        dispatcher.put(EmptyStmt.class, (EmptyStmt v, Object a) -> visit(v, a));
        dispatcher.put(ExpressionStmt.class, (ExpressionStmt v, Object a) -> visit(v, a));
        dispatcher.put(SwitchStmt.class, (SwitchStmt v, Object a) -> visit(v, a));
        dispatcher.put(SwitchEntry.class, (SwitchEntry v, Object a) -> visit(v, a));
        dispatcher.put(BreakStmt.class, (BreakStmt v, Object a) -> visit(v, a));
        dispatcher.put(ReturnStmt.class, (ReturnStmt v, Object a) -> visit(v, a));
        dispatcher.put(IfStmt.class, (IfStmt v, Object a) -> visit(v, a));
        dispatcher.put(WhileStmt.class, (WhileStmt v, Object a) -> visit(v, a));
        dispatcher.put(ContinueStmt.class, (ContinueStmt v, Object a) -> visit(v, a));
        dispatcher.put(DoStmt.class, (DoStmt v, Object a) -> visit(v, a));
        dispatcher.put(ForEachStmt.class, (ForEachStmt v, Object a) -> visit(v, a));
        dispatcher.put(ForStmt.class, (ForStmt v, Object a) -> visit(v, a));
        dispatcher.put(ThrowStmt.class, (ThrowStmt v, Object a) -> visit(v, a));
        dispatcher.put(SynchronizedStmt.class, (SynchronizedStmt v, Object a) -> visit(v, a));
        dispatcher.put(TryStmt.class, (TryStmt v, Object a) -> visit(v, a));
        dispatcher.put(CatchClause.class, (CatchClause v, Object a) -> visit(v, a));
        dispatcher.put(LambdaExpr.class, (LambdaExpr v, Object a) -> visit(v, a));
        dispatcher.put(MethodReferenceExpr.class, (MethodReferenceExpr v, Object a) -> visit(v, a));
        dispatcher.put(TypeExpr.class, (TypeExpr v, Object a) -> visit(v, a));
        dispatcher.put(NodeList.class, (NodeList v, Object a) -> visit(v, a));
        dispatcher.put(Name.class, (Name v, Object a) -> visit(v, a));
        dispatcher.put(SimpleName.class, (SimpleName v, Object a) -> visit(v, a));
        dispatcher.put(ImportDeclaration.class, (ImportDeclaration v, Object a) -> visit(v, a));
        dispatcher.put(ModuleDeclaration.class, (ModuleDeclaration v, Object a) -> visit(v, a));
        dispatcher.put(ModuleRequiresDirective.class, (ModuleRequiresDirective v, Object a) -> visit(v, a));
        dispatcher.put(ModuleExportsDirective.class, (ModuleExportsDirective v, Object a) -> visit(v, a));
        dispatcher.put(ModuleProvidesDirective.class, (ModuleProvidesDirective v, Object a) -> visit(v, a));
        dispatcher.put(ModuleUsesDirective.class, (ModuleUsesDirective v, Object a) -> visit(v, a));
        dispatcher.put(ModuleOpensDirective.class, (ModuleOpensDirective v, Object a) -> visit(v, a));
        dispatcher.put(UnparsableStmt.class, (UnparsableStmt v, Object a) -> visit(v, a));
        dispatcher.put(ReceiverParameter.class, (ReceiverParameter v, Object a) -> visit(v, a));
        dispatcher.put(VarType.class, (VarType v, Object a) -> visit(v, a));
        dispatcher.put(Modifier.class, (Modifier v, Object a) -> visit(v, a));
        dispatcher.put(SwitchExpr.class, (SwitchExpr v, Object a) -> visit(v, a));
        dispatcher.put(YieldStmt.class, (YieldStmt v, Object a) -> visit(v, a));
        dispatcher.put(TextBlockLiteralExpr.class, (TextBlockLiteralExpr v, Object a) -> visit(v, a));
    }

    private Visitable dispatch(Visitable v, Object arg) {
        VisitorFunction<Visitable, Object, Visitable> f = (VisitorFunction<Visitable, Object, Visitable>) dispatcher
                .get(v.getClass());
        return f.apply(v, arg);
    }

    @Override
    public Visitable visit(CompilationUnit n, Object arg) {
        System.out.println("CompilationUnit " + n);
        return null;
    }

    @Override
    public Visitable visit(PackageDeclaration n, Object arg) {
        System.out.println("PackageDeclaration " + n);
        return null;
    }

    @Override
    public Visitable visit(TypeParameter n, Object arg) {
        System.out.println("TypeParameter " + n);
        return null;
    }

    @Override
    public Visitable visit(LineComment n, Object arg) {
        System.out.println("LineComment " + n);
        return null;
    }

    @Override
    public Visitable visit(BlockComment n, Object arg) {
        System.out.println("BlockComment " + n);
        return null;
    }

    @Override
    public Visitable visit(ClassOrInterfaceDeclaration n, Object arg) {
        System.out.println("ClassOrInterfaceDeclaration " + n);
        return null;
    }

    @Override
    public Visitable visit(EnumDeclaration n, Object arg) {
        System.out.println("EnumDeclaration " + n);
        return null;
    }

    @Override
    public Visitable visit(EnumConstantDeclaration n, Object arg) {
        System.out.println("EnumConstantDeclaration " + n);
        return null;
    }

    @Override
    public Visitable visit(AnnotationDeclaration n, Object arg) {
        System.out.println("AnnotationDeclaration " + n);
        return null;
    }

    @Override
    public Visitable visit(AnnotationMemberDeclaration n, Object arg) {
        System.out.println("AnnotationMemberDeclaration " + n);
        return null;
    }

    @Override
    public Visitable visit(FieldDeclaration n, Object arg) {
        System.out.println("FieldDeclaration " + n);
        return null;
    }

    @Override
    public Visitable visit(VariableDeclarator n, Object arg) {
        System.out.println("VariableDeclarator " + n);
        return null;
    }

    @Override
    public Visitable visit(ConstructorDeclaration n, Object arg) {
        System.out.println("ConstructorDeclaration " + n);
        return null;
    }

    @Override
    public Visitable visit(MethodDeclaration n, Object arg) {
        System.out.println("MethodDeclaration " + n);
        return null;
    }

    @Override
    public Visitable visit(Parameter n, Object arg) {
        System.out.println("Parameter " + n);
        return null;
    }

    @Override
    public Visitable visit(InitializerDeclaration n, Object arg) {
        System.out.println("InitializerDeclaration " + n);
        return null;
    }

    @Override
    public Visitable visit(JavadocComment n, Object arg) {
        System.out.println("JavadocComment " + n);
        return null;
    }

    @Override
    public Visitable visit(ClassOrInterfaceType n, Object arg) {
        System.out.println("ClassOrInterfaceType " + n);
        return null;
    }

    @Override
    public Visitable visit(PrimitiveType n, Object arg) {
        System.out.println("PrimitiveType " + n);
        return null;
    }

    @Override
    public Visitable visit(ArrayType n, Object arg) {
        System.out.println("ArrayType " + n);
        return null;
    }

    @Override
    public Visitable visit(ArrayCreationLevel n, Object arg) {
        System.out.println("ArrayCreationLevel " + n);
        return null;
    }

    @Override
    public Visitable visit(IntersectionType n, Object arg) {
        System.out.println("IntersectionType " + n);
        return null;
    }

    @Override
    public Visitable visit(UnionType n, Object arg) {
        System.out.println("UnionType " + n);
        return null;
    }

    @Override
    public Visitable visit(VoidType n, Object arg) {
        System.out.println("VoidType " + n);
        return null;
    }

    @Override
    public Visitable visit(WildcardType n, Object arg) {
        System.out.println("WildcardType " + n);
        return null;
    }

    @Override
    public Visitable visit(UnknownType n, Object arg) {
        System.out.println("UnknownType " + n);
        return null;
    }

    @Override
    public Visitable visit(ArrayAccessExpr n, Object arg) {
        System.out.println("ArrayAccessExpr " + n);
        return null;
    }

    @Override
    public Visitable visit(ArrayCreationExpr n, Object arg) {
        System.out.println("ArrayCreationExpr " + n);
        return null;
    }

    @Override
    public Visitable visit(ArrayInitializerExpr n, Object arg) {
        System.out.println("ArrayInitializerExpr " + n);
        return null;
    }

    @Override
    public Visitable visit(AssignExpr n, Object arg) {
        System.out.println("AssignExpr " + n);
        return null;
    }

    @Override
    public Visitable visit(BinaryExpr n, Object arg) {
        System.out.println("BinaryExpr " + n);
        return null;
    }

    @Override
    public Visitable visit(CastExpr n, Object arg) {
        System.out.println("CastExpr " + n);
        return null;
    }

    @Override
    public Visitable visit(ClassExpr n, Object arg) {
        System.out.println("ClassExpr " + n);
        return null;
    }

    @Override
    public Visitable visit(ConditionalExpr n, Object arg) {
        System.out.println("ConditionalExpr " + n);
        return null;
    }

    @Override
    public Visitable visit(EnclosedExpr n, Object arg) {
        System.out.println("EnclosedExpr " + n);
        return null;
    }

    @Override
    public Visitable visit(FieldAccessExpr n, Object arg) {
        System.out.println("FieldAccessExpr " + n);
        return null;
    }

    @Override
    public Visitable visit(InstanceOfExpr n, Object arg) {
        System.out.println("InstanceOfExpr " + n);
        return null;
    }

    @Override
    public Visitable visit(StringLiteralExpr n, Object arg) {
        System.out.println("StringLiteralExpr " + n);
        return null;
    }

    @Override
    public Visitable visit(IntegerLiteralExpr n, Object arg) {
        System.out.println("IntegerLiteralExpr " + n);
        return null;
    }

    @Override
    public Visitable visit(LongLiteralExpr n, Object arg) {
        System.out.println("LongLiteralExpr " + n);
        return null;
    }

    @Override
    public Visitable visit(CharLiteralExpr n, Object arg) {
        System.out.println("CharLiteralExpr " + n);
        return null;
    }

    @Override
    public Visitable visit(DoubleLiteralExpr n, Object arg) {
        System.out.println("DoubleLiteralExpr " + n);
        return null;
    }

    @Override
    public Visitable visit(BooleanLiteralExpr n, Object arg) {
        System.out.println("BooleanLiteralExpr " + n);
        return null;
    }

    @Override
    public Visitable visit(NullLiteralExpr n, Object arg) {
        System.out.println("NullLiteralExpr " + n);
        return null;
    }

    @Override
    public Visitable visit(MethodCallExpr n, Object arg) {
        System.out.println("MethodCallExpr " + n);
        return null;
    }

    @Override
    public Visitable visit(NameExpr n, Object arg) {
        System.out.println("NameExpr " + n);
        return null;
    }

    @Override
    public Visitable visit(ObjectCreationExpr n, Object arg) {
        System.out.println("ObjectCreationExpr " + n);
        return null;
    }

    @Override
    public Visitable visit(ThisExpr n, Object arg) {
        System.out.println("ThisExpr " + n);
        return null;
    }

    @Override
    public Visitable visit(SuperExpr n, Object arg) {
        System.out.println("SuperExpr " + n);
        return null;
    }

    @Override
    public Visitable visit(UnaryExpr n, Object arg) {
        System.out.println("UnaryExpr " + n);
        return null;
    }

    @Override
    public Visitable visit(VariableDeclarationExpr n, Object arg) {
        System.out.println("VariableDeclarationExpr " + n);
        return null;
    }

    @Override
    public Visitable visit(MarkerAnnotationExpr n, Object arg) {
        System.out.println("MarkerAnnotationExpr " + n);
        return null;
    }

    @Override
    public Visitable visit(SingleMemberAnnotationExpr n, Object arg) {
        System.out.println("SingleMemberAnnotationExpr " + n);
        return null;
    }

    @Override
    public Visitable visit(NormalAnnotationExpr n, Object arg) {
        System.out.println("NormalAnnotationExpr " + n);
        return null;
    }

    @Override
    public Visitable visit(MemberValuePair n, Object arg) {
        System.out.println("MemberValuePair " + n);
        return null;
    }

    @Override
    public Visitable visit(ExplicitConstructorInvocationStmt n, Object arg) {
        System.out.println("ExplicitConstructorInvocationStmt " + n);
        return null;
    }

    @Override
    public Visitable visit(LocalClassDeclarationStmt n, Object arg) {
        System.out.println("LocalClassDeclarationStmt " + n);
        return null;
    }

    @Override
    public Visitable visit(AssertStmt n, Object arg) {
        System.out.println("AssertStmt " + n);
        return null;
    }

    @Override
    public Visitable visit(BlockStmt n, Object arg) {
        System.out.println("BlockStmt " + n);
        return null;
    }

    @Override
    public Visitable visit(LabeledStmt n, Object arg) {
        System.out.println("LabeledStmt " + n);
        return null;
    }

    @Override
    public Visitable visit(EmptyStmt n, Object arg) {
        System.out.println("EmptyStmt " + n);
        return null;
    }

    @Override
    public Visitable visit(ExpressionStmt n, Object arg) {
        System.out.println("ExpressionStmt " + n);
        return null;
    }

    @Override
    public Visitable visit(SwitchStmt n, Object arg) {
        System.out.println("SwitchStmt " + n);
        return null;
    }

    @Override
    public Visitable visit(SwitchEntry n, Object arg) {
        System.out.println("SwitchEntry " + n);
        return null;
    }

    @Override
    public Visitable visit(BreakStmt n, Object arg) {
        System.out.println("BreakStmt " + n);
        return null;
    }

    @Override
    public Visitable visit(ReturnStmt n, Object arg) {
        System.out.println("ReturnStmt " + n);
        return null;
    }

    @Override
    public Visitable visit(IfStmt n, Object arg) {
        System.out.println("IfStmt " + n);
        return null;
    }

    @Override
    public Visitable visit(WhileStmt n, Object arg) {
        System.out.println("WhileStmt " + n);
        return null;
    }

    @Override
    public Visitable visit(ContinueStmt n, Object arg) {
        System.out.println("ContinueStmt " + n);
        return null;
    }

    @Override
    public Visitable visit(DoStmt n, Object arg) {
        System.out.println("DoStmt " + n);
        return null;
    }

    @Override
    public Visitable visit(ForEachStmt n, Object arg) {
        System.out.println("ForEachStmt " + n);
        return null;
    }

    @Override
    public Visitable visit(ForStmt n, Object arg) {
        System.out.println("ForStmt " + n);
        return null;
    }

    @Override
    public Visitable visit(ThrowStmt n, Object arg) {
        System.out.println("ThrowStmt " + n);
        return null;
    }

    @Override
    public Visitable visit(SynchronizedStmt n, Object arg) {
        System.out.println("SynchronizedStmt " + n);
        return null;
    }

    @Override
    public Visitable visit(TryStmt n, Object arg) {
        System.out.println("TryStmt " + n);
        return null;
    }

    @Override
    public Visitable visit(CatchClause n, Object arg) {
        System.out.println("CatchClause " + n);
        return null;
    }

    @Override
    public Visitable visit(LambdaExpr n, Object arg) {
        System.out.println("LambdaExpr " + n);
        return null;
    }

    @Override
    public Visitable visit(MethodReferenceExpr n, Object arg) {
        System.out.println("MethodReferenceExpr " + n);
        return null;
    }

    @Override
    public Visitable visit(TypeExpr n, Object arg) {
        System.out.println("TypeExpr " + n);
        return null;
    }

    @Override
    public Visitable visit(NodeList n, Object arg) {
        System.out.println("NodeList " + n);
        return null;
    }

    @Override
    public Visitable visit(Name n, Object arg) {
        System.out.println("Name " + n);
        return null;
    }

    @Override
    public Visitable visit(SimpleName n, Object arg) {
        System.out.println("SimpleName " + n);
        return null;
    }

    @Override
    public Visitable visit(ImportDeclaration n, Object arg) {
        System.out.println("ImportDeclaration " + n);
        return null;
    }

    @Override
    public Visitable visit(ModuleDeclaration n, Object arg) {
        System.out.println("ModuleDeclaration " + n);
        return null;
    }

    @Override
    public Visitable visit(ModuleRequiresDirective n, Object arg) {
        System.out.println("ModuleRequiresDirective " + n);
        return null;
    }

    @Override
    public Visitable visit(ModuleExportsDirective n, Object arg) {
        System.out.println("ModuleExportsDirective " + n);
        return null;
    }

    @Override
    public Visitable visit(ModuleProvidesDirective n, Object arg) {
        System.out.println("ModuleProvidesDirective " + n);
        return null;
    }

    @Override
    public Visitable visit(ModuleUsesDirective n, Object arg) {
        System.out.println("ModuleUsesDirective " + n);
        return null;
    }

    @Override
    public Visitable visit(ModuleOpensDirective n, Object arg) {
        System.out.println("ModuleOpensDirective " + n);
        return null;
    }

    @Override
    public Visitable visit(UnparsableStmt n, Object arg) {
        System.out.println("UnparsableStmt " + n);
        return null;
    }

    @Override
    public Visitable visit(ReceiverParameter n, Object arg) {
        System.out.println("ReceiverParameter " + n);
        return null;
    }

    @Override
    public Visitable visit(VarType n, Object arg) {
        System.out.println("VarType " + n);
        return null;
    }

    @Override
    public Visitable visit(Modifier n, Object arg) {
        System.out.println("Modifier " + n);
        return null;
    }

    @Override
    public Visitable visit(SwitchExpr n, Object arg) {
        System.out.println("SwitchExpr " + n);
        return null;
    }

    @Override
    public Visitable visit(YieldStmt n, Object arg) {
        System.out.println("YieldStmt " + n);
        return null;
    }

    @Override
    public Visitable visit(TextBlockLiteralExpr n, Object arg) {
        System.out.println("TextBlockLiteralExpr " + n);
        return null;
    }

}
