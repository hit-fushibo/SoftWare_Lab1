package org.example;

import java.util.ArrayList;

public class NodeEntity {
    private final String name;
    //java数组EdgeEntity[]
    private ArrayList<EdgeEntity> edges;

    public NodeEntity(String name) {
        this.name = name;
        //ArrayList
        edges = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<EdgeEntity> getEdges() {
        return edges;
    }

    public void addEdges(String dest) {
        for (EdgeEntity edge : edges){
            if (edge.getEndNode().equals(dest)){
                edge.addWeight();
                return ;
            }
        }
        EdgeEntity edgeEntity = new EdgeEntity(dest);
        edges.add(edgeEntity);

    }
}
