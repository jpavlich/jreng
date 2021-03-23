package co.edu.javeriana.jreng.dep;

public enum NodeType {
    CLASS("Class"), METHOD("Method"), FIELD("Field"), PARAM("Parameter"), CONSTRUCTOR("Constructor"),
    INTERFACE("Interface"), VARIABLE("Variable"), METHOD_CALL("Variable"), ANNOTATION ("Annotation"),

    ;

    private String type;

    private NodeType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
