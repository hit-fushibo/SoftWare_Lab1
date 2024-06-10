package org.example;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class MyGraph {
    //wordGraph代表整个有向图，最外层的ArrayList表示一个结点
    private final ArrayList<NodeEntity> nodeGraph;
//    private final ArrayList<ArrayList<Map<Integer, Integer>>> wordGraph;
    private final Map<Integer, String> ItoS;
    private final Map<String, Integer> StoI;

    private String randomWalkNodeName;

    //构造器
    public MyGraph() {
        this.nodeGraph = new ArrayList<>();
//        this.wordGraph = new ArrayList<>();
        this.ItoS = new HashMap<>();
        this.StoI = new HashMap<>();
    }

    public void addNode(String name) {
        //查询现有图中是否有name字符串结点
        if (!this.StoI.containsKey(name)) {
            //如果没有包含，说明是新结点，要加入图中
            int nodeNum = nodeGraph.size();
            ItoS.put(nodeNum, name);
            StoI.put(name, nodeNum);
            nodeGraph.add(new NodeEntity(name));
        }
    }

    public void addRelation(String source, String destination) {
        int sourceIndex = StoI.get(source);
        int destIndex = StoI.get(destination);
        int flag = 1;
        NodeEntity sourceNode = nodeGraph.get(sourceIndex);
        sourceNode.addEdges(nodeGraph.get(destIndex).getName());

    }


    //获取整个图的边权重集合，Map的键String表示节点名，值也是Map形式，进一步根据目标节点的名称查该边权重
    public Map<String, Map<String, Integer>> getGraph() {
        Map<String, Map<String, Integer>> graph = new HashMap<>();
        for (int i = 0; i < nodeGraph.size(); i++) {
            //针对图的每个节点，获取边集，以Map<String, Integer>格式存储
            String sourceName = ItoS.get(i);
            NodeEntity node = nodeGraph.get(i);
            ArrayList<EdgeEntity> nodeEdge = node.getEdges();
            Map<String, Integer> edgeMap = new HashMap<>();
            for (EdgeEntity item : nodeEdge) {
                edgeMap.put(item.getEndNode(),item.getWeight());
            }
            graph.put(sourceName, edgeMap);
        }
        return graph;
    }

    //获取整个图的边集合，根据String（节点名称查）
    public Map<String, ArrayList<String>> getVGraph() {
        Map<String, ArrayList<String>> graph = new HashMap<>();
        for (int i = 0; i < nodeGraph.size(); i++) {
            String sourceName = ItoS.get(i);
            NodeEntity node = nodeGraph.get(i);
            ArrayList<EdgeEntity> edges = node.getEdges();
            ArrayList<String> edgeName = new ArrayList<>();
            for(EdgeEntity item:edges){
                edgeName.add(new String(item.getEndNode()));
            }
            graph.put(sourceName, edgeName);
        }
        return graph;
    }

    //查询桥接词
    public ArrayList<String> searchBridgeWord(String sour, String dest, AtomicInteger isError) {
        if (inputCheck(sour, dest, isError)) return new ArrayList<>();

        ArrayList<String> bridgeWords = new ArrayList<>();
        int sIndex = this.StoI.get(sour);
        ArrayList<EdgeEntity> edgeList = nodeGraph.get(sIndex).getEdges();
        for(EdgeEntity item: edgeList){
            NodeEntity bridgeNode = nodeGraph.get(StoI.get(item.getEndNode()));
            for(EdgeEntity bridge : bridgeNode.getEdges()){
                if(bridge.getEndNode().equals(dest)){
                    bridgeWords.add(bridgeNode.getName());
                    break;
                }
            }
        }
        return bridgeWords;
    }

    private boolean inputCheck(String sour, String dest, AtomicInteger isError) {
        if (!StoI.containsKey(sour)) {
            isError.set(1);

            if (!StoI.containsKey(dest)) {
                isError.set(3);
            }
            return true;
        }
        if (!StoI.containsKey(dest)) {
            isError.set(2);
            return true;

        }
        return false;
    }

    public String GetNewText(String originText) {
        ArrayList<String> words = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(originText, "\n\t\r ");
        while (tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken();
            word = word.replaceAll("[^a-zA-Z]", "");
            if (!word.isEmpty()) {
                words.add(word);
            }
        }
        int len = words.size();
        if (len <= 1) {
            return originText;
        } else {
            ArrayList<String> newWords = new ArrayList<>();
            Random random = new Random();
            AtomicInteger isError = new AtomicInteger(0);
            for (int i = 0; i < words.size() - 1; i++) {
                if (this.StoI.containsKey(words.get(i).toLowerCase()) && this.StoI.containsKey(words.get(i + 1).toLowerCase())) {
                    ArrayList<String> bridgeWords = this.searchBridgeWord(words.get(i).toLowerCase(), words.get(i + 1).toLowerCase(), isError);
                    if (bridgeWords.isEmpty()) {
                        newWords.add(words.get(i));
                    } else {
                        String bridgeWord = bridgeWords.get(random.nextInt(bridgeWords.size()));
                        newWords.add(words.get(i));
                        newWords.add(bridgeWord);
                    }
                } else {
                    newWords.add(words.get(i));
                }

            }
            newWords.add(words.get(words.size() - 1));
            return String.join(" ", newWords);
        }
    }

    public ArrayList<String> shortestPath(String source, String destination, AtomicInteger isError) {
        if (inputCheck(source, destination, isError)) return new ArrayList<>();
        int sourceIndex = StoI.get(source.toLowerCase());
        int destIndex = StoI.get(destination.toLowerCase());
        int graphSize = nodeGraph.size();

        Map<String,Integer> distance = new HashMap<>();
        Integer[] usedNodes = new Integer[nodeGraph.size()];
        Boolean[] searched = new Boolean[nodeGraph.size()];
        usedNodes[sourceIndex] = sourceIndex;
        for(int i = 0;i < graphSize;i++){
            usedNodes[i] = graphSize;
            searched[i] = false;
            if(i == sourceIndex){
                searched[i] = true;
                continue;
            }
            distance.put(nodeGraph.get(i).getName(),Integer.MAX_VALUE);
        }

        // 使用Dijkstra算法计算最短路径
        ArrayList<EdgeEntity> nodeEdges;
        //如果初始节点没有出边，返回空数组
        NodeEntity nodeNow = nodeGraph.get(sourceIndex);
        //nodeEdges是当前搜索节点的边集合
        nodeEdges = nodeNow.getEdges();
        if(nodeEdges.size() == 0){
            return new ArrayList<>();
        }
        for(EdgeEntity item:nodeEdges){
            distance.put(item.getEndNode(),item.getWeight());
            usedNodes[StoI.get(item.getEndNode())] = sourceIndex;
        }
        int tempDistance;
        int shortestNext = graphSize;
        int shortestDistance = 0;
        boolean flag;
        do {
            flag = false;
            shortestNext = graphSize;
            //选择当前要搜索的节点
            for(int i = 0;i < graphSize;i++){
                if(searched[i] || i == sourceIndex){
                    continue;
                }
                if(shortestNext == graphSize){
                    //只要能找到下一个要检查的节点，就继续循环
                    flag = true;
                    shortestNext = i;
                    shortestDistance = distance.get(nodeGraph.get(shortestNext).getName());
                }else{
                    if(distance.get(nodeGraph.get(i).getName()) < shortestDistance){
                        shortestNext = i;
                        shortestDistance = distance.get(nodeGraph.get(i).getName());
                    }
                }

            }
            if(shortestNext == destIndex){
                break;
            }
            nodeNow = nodeGraph.get(shortestNext);
            nodeEdges = nodeNow.getEdges();
            if(nodeEdges.size() == 0){
                searched[shortestNext] = true;
                continue;
            }

            for(EdgeEntity item : nodeEdges){
                if(item.getEndNode().equals(source))
                    continue;
                tempDistance = item.getWeight() + shortestDistance;
                if(tempDistance < distance.get(item.getEndNode())){
                    distance.put(item.getEndNode(),tempDistance);
                    usedNodes[StoI.get(item.getEndNode())] = shortestNext;
                }
            }
            searched[shortestNext] = true;
        }while (flag);

        //如果算法运行完，到目的节点的距离仍为无限大，那么说明源到目的之间没有路
        if(usedNodes[destIndex] == graphSize){
            return new ArrayList<>();
        }
        ArrayList<String> path = new ArrayList<>();
        int i = destIndex;
        while(i != sourceIndex){

            path.add(0,nodeGraph.get(i).getName());
            i = usedNodes[i];
        }
        path.add(0,nodeGraph.get(i).getName());

        return path;

    }

    public String SetRandomWalkStartNode() {
        int nodeNum = nodeGraph.size();
        Random random = new Random();
        int nodeIndex = random.nextInt(nodeNum);
        this.randomWalkNodeName = this.ItoS.get(nodeIndex);
        return this.ItoS.get(nodeIndex);
    }

    public String RandomWalkOneStep(AtomicInteger isStop) {
        int currentNodeIndex = this.StoI.get(randomWalkNodeName);
        //获取当前节点的边集合
        ArrayList<EdgeEntity> edgeList = nodeGraph.get(currentNodeIndex).getEdges();
        //如果当前节点没有出边，停止搜索
        int nextNodeNum = edgeList.size();
        if (nextNodeNum == 0) {
            isStop.set(1);
            return "";
        } else {
            //获取随机数，确定随机游走的下一个节点
            String nextNode = "";
            Random random = new Random();
            int nextNodeIndex = random.nextInt(nextNodeNum);

            EdgeEntity edge = edgeList.get(nextNodeIndex);
            nextNode = edge.getEndNode();
            this.randomWalkNodeName = nextNode;
            return nextNode;

        }
    }
    public void delGraph(){
        this.nodeGraph.clear();
        this.StoI.clear();
        this.ItoS.clear();
    }


}
