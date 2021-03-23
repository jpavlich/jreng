package co.edu.javeriana.jreng.dep;

public enum DepType {
    RETURN_TYPE("Return Type"), CALLS("Calls"), USES_CLASS("Uses Class"), USES_FIELD("Uses Field"), HAS_FIELD("Has Field"), HAS_METHOD("Has Method"),
    HAS_PARAM("Has Parameter"), HAS_ANNOTATION("Has Annotation")

    ;

    private String type;

    private DepType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
