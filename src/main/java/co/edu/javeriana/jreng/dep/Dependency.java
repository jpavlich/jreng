package co.edu.javeriana.jreng.dep;

import java.util.ArrayList;

public class Dependency extends ArrayList<String> {
    private static final long serialVersionUID = 1368163387024523895L;

    public Dependency(String src, String dst, String type) {
        add(src);
        add(dst);
        add(type);
    }

    public String getSrc() {
        return get(0);
    }

    public String getDst() {
        return get(1);
    }

    public String getType() {
        return get(2);
    }

}
