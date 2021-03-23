package co.edu.javeriana.jreng.dep;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
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
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.types.ResolvedPrimitiveType;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;

/**
 * Gives an id to every element in the AST
 */
public class Catalog {
    Pattern idPat = Pattern.compile(".*\\.([\\w\\[\\]]+(\\(.*\\))?)");

    public String shortName(String id) {
        // System.out.println(id);
        Matcher m = idPat.matcher(id);
        if (m.matches()) {
            String name = m.group(1);
            if (m.group(2) != null) {
                name = name.substring(0, m.group(1).indexOf("("));
                String[] args = m.group(2).substring(1, m.group(2).length() - 1).split(",");
                List<String> shortArgs = new ArrayList<String>();
                for (String arg : args) {
                    shortArgs.add(shortName(arg));
                }
                name += "(" + String.join(",", shortArgs) + ")";
            }
            return name;
        } else {
            return id;
        }
    }

    private String dispatch(Object obj) {
        Method m;
        try {
            m = this.getClass().getMethod("idOf", obj.getClass());
            return (String) m.invoke(this, obj);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    public String idOf(TypeDeclaration<?> clazz) {
        return clazz.getFullyQualifiedName().orElse(clazz.getNameAsString());
    }

    public String idOf(MethodDeclaration method) {
        return method.resolve().getQualifiedSignature();
    }

    public String idOf(FieldDeclaration field) {
        ResolvedFieldDeclaration rf = field.resolve();
        return rf.declaringType().getQualifiedName() + "." + rf.getName();
    }

    public String idOf(ConstructorDeclaration constr) {
        return constr.resolve().getQualifiedSignature();
    }

    public String idOf(ClassOrInterfaceType t) {
        try {
            ResolvedReferenceType rt = t.resolve();
            return rt.getQualifiedName();
        } catch (UnsupportedOperationException | UnsolvedSymbolException e) {
            return t.getNameWithScope();
        }
    }

    public String idOf(MethodCallExpr methodCall) {
        try {
            // FIXME This throws exceptions too often
            return methodCall.resolve().getQualifiedSignature();
        } catch (Throwable e) {
            System.err.println(e);
            return e.getMessage();
        }
    }

    public String idOf(VoidType vt) {
        return "void";
    }

    public String idOf(ArrayType t) {
        return t.getComponentType().asString() + "[]";
    }

    public String idOf(IntersectionType t) {
        return "intersection " + t.getElementType().asString();
    }

    public String idOf(PrimitiveType t) {
        return t.asString();
    }

    public String idOf(ReferenceTypeImpl t) {
        // return t.getQualifiedName();
        return t.getId();
    }

    public String idOf(TypeParameter t) {
        return t.toString();
    }

    public String idOf(UnionType t) {
        return t.toString();
    }

    public String idOf(UnknownType t) {
        return t.toString();
    }

    public String idOf(VarType t) {
        return t.toString();
    }

    public String idOf(WildcardType t) {
        return t.toString();
    }

    public String idOf(Type type) {
        return dispatch(type);
    }

    public String idOf(ResolvedPrimitiveType type) {
        return type.name();
    }

    public String idOf(AnnotationExpr a) {
        return a.resolve().getQualifiedName();
    }

    public String idOf(Parameter param) {
        return idOf(param.getType()) + "." + param.getNameAsString();
    }

    public String idOf(FieldAccessExpr fa) {
        try {

            return idOf(fa.resolve().getType()) + "." + fa.getNameAsString();
        } catch (Throwable e) {
            System.err.println(e);
            return fa.getNameAsString();
        }
    }

    private String idOf(ResolvedType type) {
        return dispatch(type);
    }
}