package org.example;

public class EdgeEntity {
    private final String endNode;

    private int weight;

    public EdgeEntity(String endNode) {
        this.endNode = endNode;
        this.weight = 1;
    }

    public int getWeight() {
        return weight;
    }

    public String getEndNode() {
        return endNode;
    }

    public void addWeight() {
        this.weight++;
    }
}
