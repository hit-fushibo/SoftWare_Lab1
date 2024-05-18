package org.example;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

public class Main {
    public static void main(String[] args) {
        System.setProperty("org.graphstream.ui", "default");

        Graph graph = new SingleGraph("Tutorial 1");
        graph.setStrict(false);
        graph.setAutoCreate(true);

        graph.addNode("A").addAttribute("ui.style", "shape:circle; size:30px; fill-mode:plain; fill-color:white; stroke-mode:plain; stroke-color:black; stroke-width:1px; text-alignment:center; text-size:20;");
        graph.addNode("B").addAttribute("ui.style", "shape:circle; size:30px; fill-mode:plain; fill-color:white; stroke-mode:plain; stroke-color:black; stroke-width:1px; text-alignment:center; text-size:20;");
        graph.addNode("C").addAttribute("ui.style", "shape:circle; size:30px; fill-mode:plain; fill-color:white; stroke-mode:plain; stroke-color:black; stroke-width:2px; text-alignment:center; text-size:20;");

        graph.addEdge("AB", "A", "B", true).addAttribute("ui.style", "fill-color:black; size:2px;");
        graph.addEdge("BC", "B", "C", true).addAttribute("ui.style", "fill-color:black; size:2px;");
        graph.addEdge("CA", "C", "A", true).addAttribute("ui.style", "fill-color:black; size:2px;");

        Node A = graph.getNode("A");
        Node B = graph.getNode("B");
        Node C = graph.getNode("C");

        A.addAttribute("ui.label", "node a");
        B.addAttribute("ui.label", "node b");
        C.addAttribute("ui.label", "node c");

        graph.display();
    }
}

