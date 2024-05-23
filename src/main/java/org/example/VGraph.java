package org.example;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.ArrayList;
import java.util.Iterator;
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

    /**
     * 构建图
     * @param G
     * MyGraph类的getVGraph方法返回值
     */
    public void CreatGraph(Map<String, ArrayList<String>> G) {
        Set<String> nodes = G.keySet();
        for (String node : nodes) {
            Node n = this.graph.addNode(node);
            n.setAttribute("ui.label", node); // 设置节点名称
        }
        for (String source : nodes) {
            ArrayList<String> adjList = G.get(source);
            for (String dest : adjList) {
                Edge edge = this.graph.addEdge(source + "->" + dest, source, dest, true); // 添加有向边
//                edge.setAttribute("ui.style", "size: 3px;"); // 设置边的粗细为3像素
//                edge.setAttribute("ui.color", "black"); // 设置边的颜色为黑色
//                edge.setAttribute("layout.weight", 100); // 设置边的长度为100
            }
        }

    }

    /**
     * 设置节点颜色为红色
     * @param nodeName
     * 节点名称
     */
    public void setNodeFillRed(String nodeName) {
        this.graph.getNode(nodeName).setAttribute("ui.style", "fill-color: red;");
    }

    /**
     * 设置节点颜色为蓝色
     * @param nodeName
     * 节点名称
     */
    public void setNodeFillBlue(String nodeName) {
        this.graph.getNode(nodeName).setAttribute("ui.style", "fill-color: blue;");
    }
    /**
     * 设置节点颜色为白色
     * @param nodeName
     * 节点名称
     */
    public void setNodeFillWhite(String nodeName) {
        this.graph.getNode(nodeName).setAttribute("ui.style", "fill-color: white;");
    }

    /**
     * 设置边颜色为绿色
     * @param edgeId
     * 边ID
     */
    public void setEdgeColorGreen(String edgeId) {
        this.graph.getEdge(edgeId).setAttribute("ui.style", "fill-color: green;");
    }

    /**
     * 设置所有节点为白色
     */
    public void setAllNodeWhite() {
        for (Node node : this.graph) {
            node.setAttribute("ui.style", "fill-color: white;");
        }
    }

    /**
     * 设置所有边为黑色
     */
    public void setAllEdgeDefault() {
        this.graph.edges().forEach(e -> e.setAttribute("ui.style", "fill-color: black;"));
    }

    /**
     * 清楚图的边和节点
     */
    public void clearGraph() {

        this.graph.clear();
        this.graph.setAttribute("ui.quality");
        this.graph.setAttribute("ui.antialias");

        // 设置默认样式
        this.graph.setAttribute("ui.stylesheet", "node {shape: circle; size: 80px, 80px; fill-color: white; stroke-mode: plain; stroke-color: black; text-alignment: center; text-size: 20px;}");


    }
}
