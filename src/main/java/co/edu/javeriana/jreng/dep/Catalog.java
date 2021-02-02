package co.edu.javeriana.jreng.dep;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.types.ResolvedReferenceType;

public class Catalog {
    public String idOf(TypeDeclaration<?> clazz) {
        return clazz.getFullyQualifiedName().orElse(clazz.getNameAsString());
    }

    public String idOf(MethodDeclaration method) {
        return method.resolve().getQualifiedSignature();
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
        return methodCall.resolve().getQualifiedSignature();
    }

    public String idOf(Type type) {
        Method m;
        try {
            m = this.getClass().getMethod("idOf", type.getClass());
            return (String) m.invoke(this, type);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

}
