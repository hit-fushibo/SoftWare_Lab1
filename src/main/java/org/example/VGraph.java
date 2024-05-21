package org.example;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class VGraph {
    public Graph graph;

    public VGraph(String graphName) {
        this.graph = new SingleGraph(graphName);
        this.graph.setAttribute("ui.quality");
        this.graph.setAttribute("ui.antialias");

        // 设置默认样式
        this.graph.setAttribute("ui.stylesheet", "node {shape: circle; size: 80px, 80px; fill-color: white; stroke-mode: plain; stroke-color: black; text-alignment: center; text-size: 20px;}");
    }

    public void CreatGraph(Map<String, ArrayList<String>> G) {
        Set<String> nodes = G.keySet();
        for (String node : nodes) {
            Node n = this.graph.addNode(node);
            n.setAttribute("ui.label", node); // 设置节点名称
        }
        for (String source : nodes) {
            ArrayList<String> adjList = G.get(source);
            for (String dest : adjList) {
                this.graph.addEdge(source + "->" + dest, source, dest, true); // 添加有向边
            }
        }

    }

    public void setNodeFillRed(String nodeName) {
        this.graph.getNode(nodeName).setAttribute("ui.style", "fill-color: red;");
    }

    public void setNodeFillBlue(String nodeName) {
        this.graph.getNode(nodeName).setAttribute("ui.style", "fill-color: blue;");
    }

    public void setNodeFillWhite(String nodeName) {
        this.graph.getNode(nodeName).setAttribute("ui.style", "fill-color: white;");
    }

    public void setEdgeColorGreen(String edgeId) {
        this.graph.getEdge(edgeId).setAttribute("ui.style", "fill-color: green;");
    }

    public void setAllNodeWhite() {
        for (Node node : this.graph) {
            node.setAttribute("ui.style", "fill-color: white;");
        }
    }

    public void setAllEdgeDefault() {
        this.graph.edges().forEach(e -> e.setAttribute("ui.style", "fill-color: black;"));
    }

    public void clearGraph() {
        // 清除所有节点
        for (Node node : this.graph) {
            node.removeAttribute("ui.label");
            node.clearAttributes();
            this.graph.removeNode(node);
        }

        // 清除所有边
        this.graph.edges().forEach(e -> this.graph.removeEdge(e));

    }
}
