package org.example;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MyGraph {
    private ArrayList<ArrayList<Map<Integer, Integer>>> wordGraph;
    private Map<Integer, String> ItoS;
    private Map<String, Integer> StoI;

    private String randomWalkNodeName;

    public MyGraph() {
        this.wordGraph = new ArrayList<>();
        this.ItoS = new HashMap<>();
        this.StoI = new HashMap<>();
    }
    public void delGraph(){
        this.wordGraph = new ArrayList<>();
        this.ItoS = new HashMap<>();
        this.StoI = new HashMap<>();
    }

    /**
     * 向图中添加一个节点
     * @param name
     * 节点名称
     */
    public void addNode(String name) {
        if (!this.StoI.containsKey(name)) {
            int nodeNum = wordGraph.size();
            ItoS.put(nodeNum, name);
            StoI.put(name, nodeNum);
            ArrayList<Map<Integer, Integer>> list = new ArrayList<>();
            wordGraph.add(list);
        }
    }

    /**
     * 向图中添加一条边
     * @param source
     * 边起点
     * @param destination
     * 边终点
     */

    public void addRelation(String source, String destination) {
        int sourceIndex = StoI.get(source);
        int destIndex = StoI.get(destination);
        int flag = 1;
        ArrayList<Map<Integer, Integer>> sourceList = wordGraph.get(sourceIndex);
        for (Map<Integer, Integer> edge : sourceList) {
            if (edge.containsKey(destIndex)) {
                int currentWeight = edge.get(destIndex);
                edge.remove(destIndex); // 删除原始权值记录
                edge.put(destIndex, currentWeight + 1); // 更新边的权值
                flag = 0;
            }
        }
        if (flag == 1) {
            Map<Integer, Integer> newEdge = new HashMap<>();
            newEdge.put(destIndex, 1); // 新建边，权值为1
            sourceList.add(newEdge);
        }

    }

    /**
     * 获取图结构，与fileIO的OutPutEdges方法一起输出图结构。
     * @return
     * 返回一个Map，其中每一个键值对代表从一个节点出发的所有边。其值为一个Map，每一个键值代表
     * 边的终点以及权值。
     */
    public Map<String, Map<String, Integer>> getGraph() {
        Map<String, Map<String, Integer>> graph = new HashMap<>();
        for (int i = 0; i < this.wordGraph.size(); i++) {
            String sourceName = this.ItoS.get(i);
            ArrayList<Map<Integer, Integer>> adjList = this.wordGraph.get(i);
            Map<String, Integer> newEdge = new HashMap<>();
            for (Map<Integer, Integer> edge : adjList) {

                Set<Integer> distIndex = edge.keySet();
                for (int dist_index : distIndex) {
                    String distName = this.ItoS.get(dist_index);
                    int weight = edge.get(dist_index);

                    newEdge.put(distName, weight);

                }


            }
            graph.put(sourceName, newEdge);
        }
        return graph;
    }

    /**
     * 与getGraph类似，但是不返回边权值信息。与VGraph类的CreateGraph方法一起构建可视化图
     * @return
     * 返回一个Map，其中每一个键值对代表从一个节点出发的所有边。其值为一个List，代表边的终点。
     */
    public Map<String, ArrayList<String>> getVGraph() {
        Map<String, ArrayList<String>> graph = new HashMap<>();
        for (int i = 0; i < this.wordGraph.size(); i++) {
            String sourceName = this.ItoS.get(i);
            ArrayList<Map<Integer, Integer>> adjList = this.wordGraph.get(i);
            ArrayList<String> edges = new ArrayList<>();
            for (Map<Integer, Integer> edge : adjList) {
                Set<Integer> destIndexes = edge.keySet();
                for (int destIndex : destIndexes) {
                    String destName = this.ItoS.get(destIndex);
                    edges.add(destName);
                }
            }
            graph.put(sourceName, edges);
        }
        return graph;
    }


    /**
     * 查询给定word1到word2的桥接词
     * @param sour
     * word1
     * @param dest
     * word2
     * @param isError
     * 用于指示是否出错。0-无错误；1-word1不存在；2-word2不存在；3-word1和2都不存在。
     * @return
     * 返回word1到word2的所有桥接词构成的List，为空代表无桥接词。
     */
    public ArrayList<String> searchBridgeWord(String sour, String dest, AtomicInteger isError) {
        if (inputCheck(sour, dest, isError)) return new ArrayList<>();

        ArrayList<String> bridgeWords = new ArrayList<>();
        int sIndex = this.StoI.get(sour);
        int dIndex = this.StoI.get(dest);
        ArrayList<Map<Integer, Integer>> adjList = this.wordGraph.get(sIndex);
        for (Map<Integer, Integer> edge : adjList) {
            Set<Integer> mIndexes = edge.keySet();
            for (int mIndex : mIndexes) {
                ArrayList<Map<Integer, Integer>> mAdjList = this.wordGraph.get(mIndex);
                for (Map<Integer, Integer> mEdge : mAdjList) {
                    if (mEdge.containsKey(dIndex)) {
                        String bridgeWord = this.ItoS.get(mIndex);
                        bridgeWords.add(bridgeWord);
                    }
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

    /**
     * 根据输入构建新的输出，功能4
     * @param originText
     * 原始文本，假设只以空格作为分隔符，无换行符以及非大小写字母字符
     * @return
     * 构建的新文本
     */
    public String GetNewText(String originText) {
        String[] a = originText.split(" ");
        ArrayList<String> words = new ArrayList<>(Arrays.asList(a));
        int len = words.size();
        if (len <= 1) {
            return originText;
        } else {
            ArrayList<String> newWords = new ArrayList<>();
            Random random = new Random();
            AtomicInteger isError = new AtomicInteger(0);
            for (int i = 0; i < words.size() - 1; i++) {
                if (this.StoI.containsKey(words.get(i)) && this.StoI.containsKey(words.get(i + 1))) {
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

    /**
     * 查询word1到word2的最短路径
     * @param source
     * word1
     * @param destination
     * word2
     * @param isError
     * 用于指示是否出错。0-无错误；1-word1不存在；2-word2不存在；3-word1和2都不存在。
     * @return
     * 由最短路径依次经过的节点名称构成的List，为空代表无最短路径即不可达。
     */
    public ArrayList<String> shortestPath(String source, String destination, AtomicInteger isError) {
        if (inputCheck(source, destination, isError)) return new ArrayList<>();
        int sourceIndex = StoI.get(source.toLowerCase());
        int destIndex = StoI.get(destination.toLowerCase());

        // 使用Dijkstra算法计算最短路径
        PriorityQueue<Map.Entry<Integer, Integer>> pq = new PriorityQueue<>(Comparator.comparingInt(Map.Entry::getValue));
        Map<Integer, Integer> distance = new HashMap<>();
        Map<Integer, Integer> parent = new HashMap<>();
        for (int i = 0; i < wordGraph.size(); i++) {
            distance.put(i, Integer.MAX_VALUE);
            parent.put(i, -1);
        }

        distance.put(sourceIndex, 0);
        pq.offer(new AbstractMap.SimpleEntry<>(sourceIndex, 0));

        while (!pq.isEmpty()) {
            Map.Entry<Integer, Integer> entry = pq.poll();
            int u = entry.getKey();
            int distU = entry.getValue();

            if (distU > distance.get(u)) {
                continue;
            }

            for (Map<Integer, Integer> edge : wordGraph.get(u)) {
                for (int v : edge.keySet()) {
                    int weight = edge.get(v);
                    int newDist = distU + weight;

                    if (newDist < distance.get(v)) {
                        distance.put(v, newDist);
                        parent.put(v, u);
                        pq.offer(new AbstractMap.SimpleEntry<>(v, newDist));
                    }
                }
            }
        }

        // 构造路径
        ArrayList<String> path = new ArrayList<>();
        int current = destIndex;
        while (current != -1) {
            path.add(0, ItoS.get(current));
            current = parent.get(current);
        }

        return path;
    }

    /**
     * 设置随机游走起点
     * @return
     * 起点名称
     */
    public String SetRandomWalkStartNode() {
        int nodeNum = this.wordGraph.size();
        Random random = new Random();
        int nodeIndex = random.nextInt(nodeNum);
        this.randomWalkNodeName = this.ItoS.get(nodeIndex);
        return this.ItoS.get(nodeIndex);
    }

    /**
     * 单步随机游走
     * @param isStop
     * 指示随机游走是否停止，遇到重复边或无出边
     * @return
     * 游走到的节点名称
     */
    public String RandomWalkOneStep(AtomicInteger isStop) {
        int currentNodeIndex = this.StoI.get(randomWalkNodeName);
        ArrayList<Map<Integer, Integer>> adjList = this.wordGraph.get(currentNodeIndex);
        int nextNodeNum = adjList.size();
        if (nextNodeNum == 0) {
            isStop.set(1);
            return "";
        } else {
            String nextNode = "";
            Random random = new Random();
            int nextNodeIndex = random.nextInt(nextNodeNum);
            Map<Integer, Integer> edge = adjList.get(nextNodeIndex);
            Set<Integer> nextNodeIndexes = edge.keySet();
            for (int i : nextNodeIndexes) {
                nextNode = this.ItoS.get(i);
            }
            this.randomWalkNodeName = nextNode;
            return nextNode;

        }
    }


}
