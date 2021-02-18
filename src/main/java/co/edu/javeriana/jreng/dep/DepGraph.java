package co.edu.javeriana.jreng.dep;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DepGraph {
    private Set<List<String>> deps = new HashSet<>();
    private Map<String, List<String>> nodes = new HashMap<>();

    public DepGraph() {
    }

    public void addNode(String id, NodeType t) {
        nodes.put(id, new Node(id, t.getType()));
    }

    public void addDep(String src, String dst, DepType t) {
        deps.add(new Dependency(src, dst, t.getType()));
    }

    public boolean hasNode(String id) {
        return nodes.containsKey(id);
    }

    public Set<List<String>> getDeps() {
        return deps;
    }

    public Collection<List<String>> getNodes() {
        return nodes.values();
    }

}
