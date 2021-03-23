package co.edu.javeriana.jreng.dep;

public enum NodeSubtype {
    REPOSITORY("Repository"), CONTROLLER("Controller"), REST_CONTROLLER("Rest Controller"), ENTITY("Entity"),
    SERVICE("Service"), NONE("");

    private String subtype;

    private NodeSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getSubtype() {
        return subtype;
    }

}
