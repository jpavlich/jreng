package co.edu.javeriana.jreng.dep;

import java.util.ArrayList;

public class Node extends ArrayList<String> {

    private static final long serialVersionUID = -2874826030332811949L;

    public Node(String id, String label, String type, String subtype) {
        add(id);
        add(label);
        add(type);
        add(subtype);
    }

    public String getId() {
        return get(0);
    }

    public String getLabel() {
        return get(1);
    }

    public String getType() {
        return get(2);
    }

    public String getSubtype() {
        return get(3);
    }
}
