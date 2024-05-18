package org.example;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.ArrayList;
import java.util.Map;

public class MyGraph {
    private ArrayList<ArrayList<Map<>>> wordGraph;
    private Map<Integer,String> ItoS;
    private Map<String,Integer> StoI;

    /**一次添加一个节点
     *
     * 节点名字
     * @param name
     * 返回节点是否添加成功
     * @return
     */
    public boolean addNode(String name){

        return true;
    }

    //添加节点和添加边是分开进行的

    /**一次只能添加一条边
     *
     * 有向边起点
     * @param source
     * 有向边终点
     * @param destination
     * 添加是否成功
     * @return
     */
    public boolean addRelation(String source,String destination){

        return true;
    }

    /**
     * 函数用于输出图结构，函数内部转换图结构形式，将图以ArrayList<ArrayList<String>>形式输出
     * @return
     */
    public ArrayList<ArrayList<String>> getGraph(){

    }

    /**
     * 函数输入两个字符串，查询在图中的桥借词
     *
     * 起始节点
     * @param sour
     * 目标节点ArrayList<ArrayList<String>>
     * @param dest
     * @return
     */
    public ArrayList<String> searchBridgeWord(String sour,String dest){

        return null;
    }





}
