package co.edu.javeriana.jreng.dep;

public enum DepType {
    RETURN_TYPE("Return Type"), CALLS("Calls"), HAS("Has"), USES("Uses")

    ;

    private String type;

    private DepType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
