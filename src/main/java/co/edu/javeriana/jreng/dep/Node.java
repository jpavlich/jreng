package co.edu.javeriana.jreng.dep;

import java.util.ArrayList;

public class Node extends ArrayList<String> {

    private static final long serialVersionUID = -2874826030332811949L;

    public Node(String id, String label, String type) {
        add(id);
        add(label);
        add(type);
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
}
